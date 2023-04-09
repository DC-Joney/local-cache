package com.dc.cache.raft.processor;

import com.dc.cache.raft.ProtoUtils;
import com.google.protobuf.Message;
import com.turing.rpc.Response;

public interface RaftMultiGroupProcessor extends ReadProcessor, WriteProcessor {

    /**
     * 当前处理器是属于哪一个raft group的
     */
    String group();

    @Override
    default Response onMessage(Message message) {
        if (ProtoUtils.isRead(message))
            ReadProcessor.super.onMessage(message);
        else
            WriteProcessor.super.onMessage(message);

        return null;
    }
}
