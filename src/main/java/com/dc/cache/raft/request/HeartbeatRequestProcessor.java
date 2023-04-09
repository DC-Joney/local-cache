package com.dc.cache.raft.request;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.dc.cache.raft.discover.Member;
import com.dc.cache.server.ServerMemberManager;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.turing.common.serilizer.ProtoStuffUtils;
import com.turing.rpc.HeartBeatRequest;
import com.turing.rpc.HeartBeatResponse;
import lombok.AllArgsConstructor;

import java.time.Instant;

/**
 * 用于处理心跳请求，每隔5秒上报一次心跳，如何心跳请求失败，则剔除健康列表
 *
 * <br/>
 *
 * 如果心跳成功则定时扫描是否存在未上报心跳的Server 节点
 */
@AllArgsConstructor
public class HeartbeatRequestProcessor implements RpcProcessor<HeartBeatRequest> {

    ServerMemberManager manager;

    @Override
    public void handleRequest(RpcContext rpcCtx, HeartBeatRequest request) {
        //查看当前服务状况
        HeartBeatResponse response = HeartBeatResponse.newBuilder()
                .setSuccess(true)
                .build();

        Member member = ProtoStuffUtils.deserialize(request.getPayload().toByteArray());
        //设置状态为启动状态
        member.setStatus(Member.NodeStatus.UP);

        manager.updatePeer(member);
        rpcCtx.sendResponse(response);
    }

    @Override
    public String interest() {
        return HeartBeatRequest.class.getName();
    }
}
