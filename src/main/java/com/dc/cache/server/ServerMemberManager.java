package com.dc.cache.server;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.rpc.InvokeCallback;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.alipay.sofa.jraft.util.Endpoint;
import com.alipay.sofa.jraft.util.timer.Timeout;
import com.alipay.sofa.jraft.util.timer.Timer;
import com.alipay.sofa.jraft.util.timer.TimerTask;
import com.dc.cache.raft.MemberUtil;
import com.dc.cache.raft.discover.Member;
import com.dc.cache.raft.discover.MemberDiscover;
import com.dc.cache.raft.event.MemberChangeEventBuilder;
import com.dc.cache.raft.event.MemberChangedEvent;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.google.protobuf.UInt64Value;
import com.turing.common.TuringServiceLoader;
import com.turing.common.notify.EventPublisher;
import com.turing.common.notify.NotifyCenter;
import com.turing.common.serilizer.ProtoStuffUtils;
import com.turing.rpc.HeartBeatRequest;
import com.turing.rpc.HeartBeatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用于管理Server Member成员
 */
@Slf4j
public class ServerMemberManager implements InitializingBean {

    /**
     * 所有的节点信息
     */
    private volatile Map<String, Member> allMembers = new TreeMap<>();

    private Set<String> healthMembers = new ConcurrentSkipListSet<>();

    private final RpcClient rpcClient;

    /**
     * 用于定制扫描对应的serverMember是否存活
     */
    private final Timer managerTimer;

    private MemberDiscover discover;

    /**
     * 当前额几点
     */
    private Member self;

    public ServerMemberManager(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        this.managerTimer = JRaftUtils.raftTimerFactory().createTimer("Manage server timer task");
        afterPropertiesSet();
    }

    @Override
    public void afterPropertiesSet() {
        this.discover = TuringServiceLoader.load(MemberDiscover.class).first();
        //延迟2s进行扫描，保证节点的所有配置都已经成功加载
        managerTimer.newTimeout(new MemberLookupTask(), 2, TimeUnit.SECONDS);

        //首次执行
        managerTimer.newTimeout(new HealthCheck(), 7, TimeUnit.SECONDS);


    }

    public List<Member> allMembersWithoutSelf() {
        List<Member> members = new ArrayList<>(allMembers.values());
        members.remove(self);
        return members;
    }

    /**
     * 获取所有的健康节点信息
     */
    public Set<Member> healthServers() {
        return healthMembers.stream()
                .collect(HashSet::new, (members, address) -> members.add(allMembers.get(address)), Set::addAll);
    }


    public synchronized void memberJoin(Collection<Member> members) {
        Set<Member> set = new HashSet<>(members);
        set.addAll(allMembers.values());
        memberChange(set);
    }

    /**
     * members leave this cluster.
     *
     * @param members {@link Collection} wait leave members
     * @return is success
     */
    public synchronized void memberLeave(Collection<Member> members) {
        Set<Member> set = new HashSet<>(allMembers.values());
        set.removeAll(members);
        memberChange(set);
    }

    /**
     * this member {@link Member#getStatus()} ()} is health.
     *
     * @param address ip:port
     * @return is health
     */
    public boolean isUnHealth(String address) {
        Member member = allMembers.get(address);
        if (member == null) {
            return false;
        }
        return !Member.NodeStatus.UP.equals(member.getStatus());
    }

    public boolean updatePeer(Member newMember) {
        String address = newMember.getAddress();
        if (!allMembers.containsKey(address)) {
            log.warn("address {} want to update Member, but not in member list!", newMember.getAddress());
            return false;
        }

        allMembers.computeIfPresent(address, (s, member) -> {

            if (newMember.getStatus().isDown()) {
                healthMembers.remove(newMember.getAddress());
            }

            //判断节点的信息是否改变了
            boolean isPublishChangeEvent = MemberUtil.isBasicInfoChanged(newMember, member);

            //添加节点最后上报心跳的时间
            newMember.addExtended(Member.LAST_REFRESH_TIME, System.currentTimeMillis());
            member.copyFrom(newMember);

            //如果节点信息更新了，则触发MemberChange 事件
            if (isPublishChangeEvent) {
                // member basic data changes and all listeners need to be notified
//                notifyMemberChange(member);
            }
            return member;
        });

        return true;
    }

    /**
     * 出发Member Change
     */
    private void notifyMemberChange(Member member) {
        MemberChangedEvent memberChangedEvent = MemberChangeEventBuilder.create()
                .members(allMembers.values())
                .trigger(member)
                .build();

        NotifyCenter.publishEvent(memberChangedEvent);
    }


    private void memberChange(Set<Member> newMembers) {

        boolean membersChanged = true;

        //找出需要被删除的节点
        Set<Member> removeMembers = Sets.newHashSet(ServerMemberManager.this.allMembers.values());
        removeMembers.removeAll(newMembers);
        //找出需要添加的节点
        newMembers.removeAll(ServerMemberManager.this.allMembers.values());

        EventPublisher<MemberChangedEvent> publisher = NotifyCenter.getPublisher(MemberChangedEvent.class);

        Set<PeerId> removePeers = removeMembers
                .stream()
                .map(Member::toPeer)
                .collect(Collectors.toSet());

        Set<PeerId> addPeers = newMembers
                .stream()
                .map(Member::toPeer)
                .collect(Collectors.toSet());

        if (removePeers.isEmpty() && addPeers.isEmpty()) {
            membersChanged = false;
        }

        if (membersChanged) {
            Map<String, Member> tempMembers = new HashMap<String, Member>();
            newMembers.forEach(member -> tempMembers.putIfAbsent(member.getAddress(), member));
            this.allMembers = tempMembers;

            Set<PeerId> allPeers = allMembers.values()
                    .stream()
                    .map(Member::toPeer)
                    .collect(Collectors.toSet());

            publisher.publish(new MemberChangedEvent(removePeers, addPeers, allPeers));
        }


    }

    /**
     * 定时从discover中发现server节点，并且判断节点是否被更新
     */
    private class MemberLookupTask implements TimerTask {

        @Override
        public void run(Timeout timeout) throws Exception {
            memberChange(discover.getMembers());
            managerTimer.newTimeout(this, 5000, TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 健康检查, 查询其他Server 节点是否存活
     */
    private class HealthCheck implements TimerTask {
        @Override
        public void run(Timeout timeout) throws Exception {

            for (Member member : ServerMemberManager.this.allMembersWithoutSelf()) {
                Endpoint endpoint = member.toEndpoint();

                // check if rpc channel is available
                if (!rpcClient.checkConnection(endpoint, true)) {
                    String address = member.getAddress();
                    healthMembers.remove(address);
                    member.setStatus(Member.NodeStatus.DOWN);
                    return;
                }

                byte[] serialize = ProtoStuffUtils.serialize(member);

                //心跳请求
                HeartBeatRequest request = HeartBeatRequest
                        .newBuilder()
                        .setPayload(ByteString.copyFrom(serialize))
                        .setTimestamp(UInt64Value.newBuilder()
                                .setValue(System.currentTimeMillis()).build())
                        .build();

                // 将该ip剔除健康列表
                rpcClient.invokeAsync(endpoint, request, new InvokeCallback() {
                    @Override
                    public void complete(Object result, Throwable err) {
                        boolean reportSuccess = err == null;

                        //剔除健康列表
                        HeartBeatResponse response = (HeartBeatResponse) result;

                        if (!response.getSuccess()) {
                            reportSuccess = false;
                        }

                        //如果上报错误，则直接将其拉倒非健康列表中
                        if (!reportSuccess) {
                            String address = member.getAddress();
                            healthMembers.remove(address);
                            member.setStatus(Member.NodeStatus.DOWN);
                        }

                        //处理正常列表
                    }
                }, 1000);

            }

            managerTimer.newTimeout(this, 5, TimeUnit.SECONDS);
        }
    }

}
