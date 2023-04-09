package com.dc.cache.raft;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.dc.cache.raft.processor.Processor;
import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import com.turing.rpc.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class CacheStateMachine extends StateMachineAdapter {

    private final AtomicLong leaderTerm = new AtomicLong();

    @SneakyThrows
    @Override
    public void onApply(Iterator iter) {
        while (iter.hasNext()) {
            CacheClosure closure = null;
            Message message = null;

            if (iter.done() != null) {
                closure = (CacheClosure) iter.done();
                message = closure.getMessage();
            }else {
                ByteBuffer dataBuffer = iter.getData().duplicate();
                int flag = dataBuffer.getInt();
                Parser<Message> messageParser = MarshallerRegistryCache.getParser(flag);
                message = messageParser.parseFrom(dataBuffer.slice());
            }

            //如果closure == null 并且是读请求的话则直接忽略
            if (closure == null && ProtoUtils.isRead(message)) {
                iter.next();
                continue;
            }

            Processor processor = MarshallerRegistryCache.getProcessor(message);
            Response response = processor.onMessage(message);
            if (closure != null) {
                closure.setResponse(response);
            }

            iter.next();
        }

    }

    @Override
    public void onLeaderStart(long term) {
        leaderTerm.set(term);
        super.onLeaderStart(term);
    }

    @Override
    public void onLeaderStop(Status status) {
        if (status.isOk()) {
            leaderTerm.set(-1);
        }

        log.info(status.getErrorMsg());
        super.onLeaderStop(status);
    }

    public boolean isLeader(){
        return leaderTerm.get() > 0;
    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        return super.onSnapshotLoad(reader);
    }

    @Override
    public void onSnapshotSave(SnapshotWriter writer, Closure done) {
        super.onSnapshotSave(writer, done);
    }
}
