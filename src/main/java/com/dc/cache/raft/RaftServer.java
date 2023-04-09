package com.dc.cache.raft;

import com.alipay.sofa.jraft.*;
import com.alipay.sofa.jraft.closure.ReadIndexClosure;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.CliServiceImpl;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.entity.Task;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.option.CliOptions;
import com.alipay.sofa.jraft.option.NodeOptions;
import com.alipay.sofa.jraft.option.RaftOptions;
import com.alipay.sofa.jraft.option.RpcOptions;
import com.alipay.sofa.jraft.rpc.*;
import com.alipay.sofa.jraft.rpc.impl.GrpcRaftRpcFactory;
import com.alipay.sofa.jraft.util.Bits;
import com.alipay.sofa.jraft.util.Endpoint;
import com.alipay.sofa.jraft.util.timer.Timeout;
import com.alipay.sofa.jraft.util.timer.Timer;
import com.alipay.sofa.jraft.util.timer.TimerTask;
import com.dc.cache.raft.event.MemberChangedEvent;
import com.dc.cache.raft.event.MembersFindEvent;
import com.dc.cache.raft.processor.Processor;
import com.dc.cache.raft.request.HeartbeatRequestProcessor;
import com.dc.cache.raft.request.ReadRequestProcessor;
import com.dc.cache.raft.request.WriteRequestProcessor;
import com.dc.cache.server.ServerMemberManager;
import com.google.protobuf.Message;
import com.turing.common.notify.Event;
import com.turing.common.notify.NotifyCenter;
import com.turing.common.notify.listener.Subscriber;
import com.turing.rpc.ReadRequest;
import com.turing.rpc.Response;
import com.turing.rpc.WriteRequest;
import io.netty.util.HashedWheelTimer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.checkerframework.checker.units.qual.Time;
import org.springframework.beans.factory.InitializingBean;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class RaftServer implements InitializingBean, Subscriber<MembersFindEvent> {

    /**
     * 当前Node节点
     */
    @Getter
    private Node raftNode;

    /**
     * 当前节点ip + 端口号
     */
    private PeerId peerId;

    /**
     * 当前节点的RpcServer
     */
    private RpcServer rpcServer;

    /**
     * Raft group, 一个RaftGroup 代表一个集群
     */
    private RaftGroupService groupService;

    /**
     * 客户端配置，用于添加节点
     */
    private CliService cliService;

    /**
     * 客户端配置，用于添加节点
     */
    private CliClientService cliClientService;

    /**
     * Rpc Factory
     */
    private GrpcRaftRpcFactory rpcFactory;

    /**
     * Rpc 客户端
     */
    private RpcClient rpcClient;

    /**
     * Raft node 配置
     */
    private final NodeConfig nodeConfig;

    private static final AtomicInteger requestNum = new AtomicInteger();

    /**
     * 集群节点管理
     */
    private ServerMemberManager memberManager;

    /**
     * 用于刷新路由节点
     */
    private final Timer refreshTimer;

    private final AtomicBoolean SHUTDOWN = new AtomicBoolean(false);

    private static final String CACHE_RAFT_GROUP = "cache-raft-group";

    public RaftServer(NodeOptions nodeOptions){
        this.refreshTimer = JRaftUtils.raftTimerFactory().createTimer("Refresh route table metadata");
        this.nodeConfig = new NodeConfig();
        this.nodeConfig.nodeOptions = nodeOptions;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        rpcFactory = new GrpcRaftRpcFactory();
        //添加请求处理器
        RaftRequestProcessors.addRequestProcess(rpcFactory);

        CliOptions cliOptions = new CliOptions();
        cliOptions.setMaxRetry(3);
        cliOptions.setRpcConnectTimeoutMs(4000);
        this.cliService = RaftServiceFactory.createAndInitCliService(cliOptions);
        this.cliClientService = ((CliServiceImpl) this.cliService).getCliClientService();



        RpcOptions rpcOptions = new RpcOptions();
        rpcOptions.setEnableRpcChecksum(false);
        rpcOptions.setRpcConnectTimeoutMs(1000);
        this.rpcClient = rpcFactory.createRpcClient(rpcFactory.defaultJRaftClientConfigHelper(rpcOptions));
        RpcClient clusterRpcClient = rpcFactory.createRpcClient(rpcFactory.defaultJRaftClientConfigHelper(rpcOptions));
        this.memberManager = new ServerMemberManager(clusterRpcClient);


        this.rpcServer = rpcFactory.createRpcServer(peerId.getEndpoint());
        rpcServer.registerProcessor(new ReadRequestProcessor(this));
        rpcServer.registerProcessor(new WriteRequestProcessor(this));
        rpcServer.registerProcessor(new HeartbeatRequestProcessor(memberManager));

        RaftRpcServerFactory.addRaftRequestProcessors(rpcServer);

    }



    @Override
    public void onEvent(MembersFindEvent membersFindEvent) {
        NodeManager nodeManager = NodeManager.getInstance();
        Set<PeerId> peers = membersFindEvent.getPeers();
        Configuration conf = new Configuration();
        for (PeerId peer : peers) {
            nodeManager.addAddress(peer.getEndpoint());
            conf.addPeer(peer);
        }

        nodeConfig.configuration = conf;
        nodeConfig.nodeOptions.setInitialConf(nodeConfig.configuration);
        this.groupService = new RaftGroupService(CACHE_RAFT_GROUP, peerId, nodeConfig.nodeOptions, rpcServer);
        this.raftNode = groupService.start(false);

        //添加定时任务，定时刷新Leader信息，以及 Configuration 信息
        refreshTimer.newTimeout(new RefreshRouteTableTask(),3, TimeUnit.SECONDS);

        NotifyCenter.registerSubscriber(new Subscriber<MemberChangedEvent>() {

            @Override
            public void onEvent(MemberChangedEvent changedEvent) {
                RaftServer.this.peerChange(changedEvent.getServerList());

            }

            @Override
            public Class<? extends MemberChangedEvent> subscribeType() {
                return MemberChangedEvent.class;
            }
        });
    }

    @Override
    public Class<? extends MembersFindEvent> subscribeType() {
        return MembersFindEvent.class;
    }

    private void peerChange(Set<PeerId> serverList) {
        Configuration newPeers = serverList.stream()
                .collect(Configuration::new, Configuration::addPeer, (left, right) -> {
                    left.appendPeers(right.getPeerSet());
                });


        //如果当前节点是Leader节点，则触发ChangePeers
        if (isLeader()) {
            Status status = cliService.changePeers(CACHE_RAFT_GROUP, nodeConfig.configuration, newPeers);
            if (!status.isOk()) {
                log.error("Change new peers failed, newPeers is {}, fail cause is: {}", serverList, status.getErrorMsg());
            }
        }

        //如果changePeers 成功
        nodeConfig.configuration = newPeers;
    }

    /**
     * 判断当前节点是否是leader节点
     */
    public boolean isLeader() {
        return raftNode.isLeader();
    }

    /**
     * 读取数据，如果当前节点无法读取，则通过leader读取
     *
     * @param request 读取请求
     */
    public CompletableFuture<Response> read(ReadRequest request) {
        CompletableFuture<Response> future = new CompletableFuture<>();
        int number = requestNum.incrementAndGet();
        //用于存储请求id
        byte[] bytes = new byte[4];
        Bits.putInt(new byte[4], 0, number);
        raftNode.readIndex(bytes, new ReadIndexClosure() {
            @Override
            public void run(Status status, long index, byte[] reqCtx) {
                if (status.isOk()) {

                    Processor processor = MarshallerRegistryCache.getProcessor(request);
                    //index: leader最后提交的index
                    Response response = processor.onMessage(request);
                    //从状态机读取数据
                    future.complete(response);
                    return;
                }

                ResponseClosure closure = new ResponseClosure();
                closure.toFuture()
                        .exceptionally(ex -> Response.newBuilder().setErrMsg(ex.getMessage())
                                .setSuccess(false).build())
                        .thenAccept(future::complete);

                invokeToLeader(request, 10, closure);
            }
        });

        return future;
    }

    public CompletableFuture<Response> write(WriteRequest request) {
        CompletableFuture<Response> future = new CompletableFuture<Response>();

        if (this.isLeader()) {
            ResponseClosure responseClosure = new ResponseClosure();
            responseClosure.toFuture()
                    .exceptionally(ex -> Response.newBuilder()
                            .setSuccess(false)
                            .setErrMsg(ex.toString()).build())
                    .thenAccept(future::complete);


            Task task = new Task();
            ByteBuffer dataBuffer = getDataBuffer(request);
            task.setData(dataBuffer);
            task.setDone(new CacheClosure(request, new Closure() {
                @Override
                public void run(Status status) {
                    CacheClosure.CacheStatus cacheStatus = (CacheClosure.CacheStatus) status;
                    responseClosure.setResponse(cacheStatus.getResponse());
                    responseClosure.setThrowable(cacheStatus.getThrowable());
                    responseClosure.run(cacheStatus);
                }
            }));

            raftNode.apply(task);
            return future;
        }

        //将信息转发至leader节点
        ResponseClosure closure = new ResponseClosure();
        closure.toFuture()
                .exceptionally(ex -> Response.newBuilder()
                        .setSuccess(false)
                        .setErrMsg(ex.toString()).build())
                .thenAccept(future::complete);

        //将请求转发到Leader
        invokeToLeader(request, 10 * 1000, closure);
        return future;
    }


    /**
     * 将请求转发到leader节点
     *
     * @param request       请求
     * @param timeoutMillis 超时时间
     * @param closure       异步回调
     */
    public void invokeToLeader(final Message request, final int timeoutMillis, ResponseClosure closure) {
        try {

            //从路由列表获取leaderIp地址
            Endpoint leaderIp = RouteTable.getInstance().selectLeader(CACHE_RAFT_GROUP).getEndpoint();

            rpcClient.invokeAsync(leaderIp, request, new InvokeCallback() {
                @Override
                public void complete(Object o, Throwable ex) {
                    if (Objects.nonNull(ex)) {
                        closure.setThrowable(ex);
                        closure.run(new Status(RaftError.UNKNOWN, ex.getMessage()));
                        return;
                    }
                    if (!((Response) o).getSuccess()) {
                        closure.setThrowable(new IllegalStateException(((Response) o).getErrMsg()));
                        closure.run(new Status(RaftError.UNKNOWN, ((Response) o).getErrMsg()));
                        return;
                    }
                    closure.setResponse((Response) o);
                    closure.run(Status.OK());
                }

                @Override
                public Executor executor() {
                    return Executors.newSingleThreadExecutor();
                }
            }, timeoutMillis);
        } catch (Exception e) {
            closure.setThrowable(e);
            closure.run(new Status(RaftError.UNKNOWN, e.toString()));
        }
    }


    private ByteBuffer getDataBuffer(Message request) {
        int messageType = MarshallerRegistryCache.markWrite(request);
        int flag = ProtoUtils.addType(0, messageType);
        byte[] bytes = request.toByteArray();
        ByteBuffer dataBuffer = ByteBuffer.allocate(Integer.BYTES + bytes.length);
        dataBuffer.putInt(flag).put(bytes).flip();
        return dataBuffer;
    }

    public class RefreshRouteTableTask implements TimerTask {

        @Override
        public void run(Timeout timeout) throws Exception {

            if (SHUTDOWN.get()) {
                return;
            }

            final String groupName = CACHE_RAFT_GROUP;
            Status status = null;
            try {
                RouteTable instance = RouteTable.getInstance();
                Configuration oldConf = instance.getConfiguration(groupName);
                String oldLeader = Optional.ofNullable(instance.selectLeader(groupName)).orElse(PeerId.emptyPeer())
                        .getEndpoint().toString();
                // fix issue #3661  https://github.com/alibaba/nacos/issues/3661
                status = instance.refreshLeader(RaftServer.this.cliClientService, groupName, 10000);
                if (!status.isOk()) {
                    log.error("Fail to refresh leader for group : {}, status is : {}", groupName, status);
                }

                status = instance.refreshConfiguration(RaftServer.this.cliClientService, groupName, 10000);
                if (!status.isOk()) {
                    log.error("Fail to refresh route configuration for group : {}, status is : {}", groupName, status);
                }

                //刷新当前节点的 configuration 配置
                RaftServer.this.nodeConfig.configuration = instance.getConfiguration(CACHE_RAFT_GROUP);

            } catch (Exception e) {
                log.error("Fail to refresh raft metadata info for group : {}, error is : {}", groupName, e);
            }

            RaftServer.this.refreshTimer.newTimeout(this,5, TimeUnit.SECONDS);
        }
    }


    @Getter
    public static class MultiRaftGroup {

        private String groupName;

        private RaftGroupService groupService;

        private Node node;

    }

    public static class NodeConfig {

        private Configuration configuration = new Configuration();

        private NodeOptions nodeOptions;

    }
}
