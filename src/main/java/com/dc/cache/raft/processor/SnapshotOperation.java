package com.dc.cache.raft.processor;

import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;

public interface SnapshotOperation {

    void snapshotLoad(SnapshotReader reader);


    void snapshotWrite(SnapshotWriter writer);

}
