package com.dc.cache.raft.snapshot;

import com.alipay.sofa.jraft.entity.RaftOutter;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.dc.cache.raft.processor.SnapshotOperation;

public class MemorySnapshotOperation implements SnapshotOperation {

    @Override
    public void snapshotLoad(SnapshotReader reader) {
        RaftOutter.SnapshotMeta load = reader.load();
    }

    @Override
    public void snapshotWrite(SnapshotWriter writer) {

    }
}
