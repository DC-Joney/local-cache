package com.dc.cache.raft;

import com.turing.rpc.ReadRequest;
import com.turing.rpc.Response;
import com.turing.rpc.WriteRequest;

public abstract class RequestProcessor {

    /**
     * 读取数据
     * @param readRequest 读取请求
     */
    public abstract Response onRead(ReadRequest readRequest);

    /**
     * 写入数据
     * @param writeRequest 写入请求
     */
    public abstract Response onWrite(WriteRequest writeRequest);


}
