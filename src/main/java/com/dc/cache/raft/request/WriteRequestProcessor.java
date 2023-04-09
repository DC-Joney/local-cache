package com.dc.cache.raft.request;

import com.alipay.sofa.jraft.rpc.RpcContext;
import com.alipay.sofa.jraft.rpc.RpcProcessor;
import com.dc.cache.raft.RaftServer;
import com.turing.rpc.Response;
import com.turing.rpc.WriteRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WriteRequestProcessor implements RpcProcessor<WriteRequest> {

    private final RaftServer raftServer;

    @Override
    public void handleRequest(RpcContext rpcCtx, WriteRequest request) {
        raftServer.write(request)
                .exceptionally(ex-> Response.newBuilder()
                        .setSuccess(false)
                        .build())
                .thenAccept(rpcCtx::sendResponse);
    }

    @Override
    public String interest() {
        return WriteRequest.class.getName();
    }
}
