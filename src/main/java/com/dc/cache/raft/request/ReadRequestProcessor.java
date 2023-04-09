package com.dc.cache.raft.request;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.dc.cache.raft.RaftServer;
import com.turing.rpc.ReadRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReadRequestProcessor implements RpcProcessor<ReadRequest> {

    private final RaftServer raftServer;

    @Override
    public void handleRequest(RpcContext rpcCtx, ReadRequest request) {
        raftServer.read(request)
                .thenAccept(rpcCtx::sendResponse);
    }

    @Override
    public String interest() {
        return ReadRequest.class.getName();
    }
}
