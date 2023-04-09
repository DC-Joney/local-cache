package com.dc.cache.raft.processor;

import com.google.protobuf.Message;
import com.turing.rpc.Response;

public interface Processor {

    Response onMessage(Message message);
}
