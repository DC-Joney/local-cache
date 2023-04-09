package com.dc.cache.raft.processor;

import com.google.protobuf.Message;
import com.turing.rpc.ReadRequest;
import com.turing.rpc.Response;
import org.springframework.util.ClassUtils;

public interface ReadProcessor extends Processor{

    default Response onMessage(Message message) {
        if (message instanceof ReadRequest) {
            return onRequest((ReadRequest) message);
        }

        return Response.newBuilder().setSuccess(false)
                .setErrMsg("Cannot support type for read request  " + ClassUtils.getShortName(message.getClass()))
                .build();
    }


    Response onRequest(ReadRequest request);

}
