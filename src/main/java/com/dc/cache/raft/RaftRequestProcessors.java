package com.dc.cache.raft;

import com.alipay.sofa.jraft.rpc.impl.GrpcRaftRpcFactory;
import com.dc.cache.raft.processor.map.CaffeineProcessor;
import com.turing.rpc.ReadRequest;
import com.turing.rpc.Response;
import com.turing.rpc.WriteRequest;

public class RaftRequestProcessors {

    public static void addRequestProcess(GrpcRaftRpcFactory rpcFactory) {
        rpcFactory.registerProtobufSerializer(WriteRequest.class.getName(), WriteRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(ReadRequest.class.getName(), ReadRequest.getDefaultInstance());
        rpcFactory.registerProtobufSerializer(Response.class.getName(), Response.getDefaultInstance());
        rpcFactory.getMarshallerRegistry().registerResponseInstance(ReadRequest.class.getName(), Response.getDefaultInstance());
        rpcFactory.getMarshallerRegistry().registerResponseInstance(WriteRequest.class.getName(), Response.getDefaultInstance());
        MarshallerRegistryCache.markWrite(WriteRequest.getDefaultInstance());
        MarshallerRegistryCache.markRead(ReadRequest.getDefaultInstance());


        MarshallerRegistryCache.registerProcessor(WriteRequest.getDefaultInstance(), new CaffeineProcessor());
        MarshallerRegistryCache.registerProcessor(ReadRequest.getDefaultInstance(), new CaffeineProcessor());
    }
}
