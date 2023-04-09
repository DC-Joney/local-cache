package com.dc.cache.raft;

import cn.hutool.core.util.ClassUtil;
import com.dc.cache.raft.processor.Processor;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.turing.rpc.Response;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@UtilityClass
public class MarshallerRegistryCache {

    private final AtomicInteger typeCounter = new AtomicInteger();

    private final Map<Integer, Message> typeCache = new HashMap<>();

    private final Map<String, Integer> typeConverters = new ConcurrentHashMap<>();

    private final Map<Integer, Processor> processorCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T extends Message> Parser<T> getParser(int flag) {
        int messageType = ProtoUtils.getType(flag);
        Message message = typeCache.get(messageType);
        return (Parser<T>) message.getParserForType();
    }

    /**
     * @param message 将对应的消息类型转为 int type
     * @return 返回 message 对应的type 值
     */
    @NonNull
    public int findType(Message message) {
        String className = ClassUtil.getClassName(message, false);
        int messageType = typeConverters.computeIfAbsent(className, key -> typeCounter.getAndIncrement() << 1);
        typeCache.putIfAbsent(messageType, message);
        return messageType;
    }

    /**
     * 返回message 对应的类型
     *
     * @param message message
     */
    public int markWrite(Message message) {
        int newType = MarshallerRegistryCache.findType(message) | 1;
        MarshallerRegistryCache.setMessageType(message, newType);
        return newType;
    }

    /**
     * 返回message 对应的类型
     *
     * @param message message
     */
    public int markRead(Message message) {
        int newType = MarshallerRegistryCache.findType(message) & ~1;
        MarshallerRegistryCache.setMessageType(message, newType);
        return newType;
    }

    /**
     * @param message 将对应的消息类型转为 int type
     * @return 返回 message 对应的type 值
     */
    @NonNull
    int setMessageType(Message message, int newType) {
        int messageType = findType(message);
        Message removeMessage = typeCache.remove(messageType);
        typeCache.putIfAbsent(newType, removeMessage);
        return messageType;
    }

    public void registerProcessor(Message message, Processor processor) {
        int messageType = findType(message);
        processorCache.put(messageType, processor);
    }

    public Processor getProcessor(Message message) {
        int messageType = findType(message);
        return processorCache.getOrDefault(messageType, new Processor() {
            @Override
            public Response onMessage(Message message) {
                return Response.newBuilder()
                        .setSuccess(false)
                        .setErrMsg("Cannot find type " + messageType + " supported processor")
                        .build();
            }
        });
    }


}
