package com.dc.cache.raft.processor;

import com.google.protobuf.Message;
import com.turing.rpc.Response;
import com.turing.rpc.WriteRequest;
import org.springframework.util.ClassUtils;

public interface WriteProcessor extends Processor{

    default Response onMessage(Message message) {
        if (message instanceof WriteRequest) {
            return onRequest((WriteRequest) message);
        }

        return Response.newBuilder().setSuccess(false)
                .setErrMsg("Cannot support type for write request  " + ClassUtils.getShortName(message.getClass()))
                .build();
    }


    Response onRequest(WriteRequest request);

}
