package com.dc.cache.raft.processor.map;

import com.alibaba.fastjson.JSONObject;
import com.dc.cache.raft.processor.RaftMultiGroupProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.protobuf.ByteString;
import com.turing.rpc.ReadRequest;
import com.turing.rpc.Response;
import com.turing.rpc.WriteRequest;

import java.nio.charset.Charset;

public class CaffeineProcessor implements RaftMultiGroupProcessor {

    private LoadingCache<Object, Object> caffeineCache = null;

    @Override
    public String group() {
        return "cache";
    }

    @Override
    public Response onRequest(ReadRequest request) {
        ByteString data = request.getData();
        JSONObject jsonObject = JSONObject.parseObject(data.toString(Charset.defaultCharset()));
        Object cacheKey = jsonObject.get("cacheKey");
        Object ifPresent = caffeineCache.getIfPresent(cacheKey);
        byte[] bytes = JSONObject.toJSONBytes(ifPresent);
        return Response.newBuilder()
                .setSuccess(true)
                .setData(ByteString.copyFrom(bytes))
                .build();
    }

    @Override
    public Response onRequest(WriteRequest request) {
        ByteString data = request.getData();
        JSONObject jsonObject = JSONObject.parseObject(data.toString(Charset.defaultCharset()));
        String key = jsonObject.getString("key");
        String value = jsonObject.getString("value");
        caffeineCache.put(key, value);
        return Response.newBuilder()
                .setSuccess(true)
                .build();
    }

}
