package com.dc.cache.raft.snapshot;

import com.alipay.sofa.jraft.entity.LocalFileMetaOutter;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.dc.cache.raft.processor.SnapshotOperation;
import com.google.protobuf.ZeroByteStringHelper;
import com.turing.common.check.CRC64;

import java.util.zip.Checksum;

import static com.sun.tools.corba.se.idl.Token.Local;

public abstract class AbstractSnapshotOperation implements SnapshotOperation {

    @Override
    public void snapshotLoad(SnapshotReader reader) {
        String path = reader.getPath();


    }

    @Override
    public void snapshotWrite(SnapshotWriter writer) {
        String path = writer.getPath();

        DirectMap metaExtension = new DirectMap();
        Checksum checksum = new CRC64();
        checksum.update(path);

        LocalFileMetaOutter.LocalFileMeta fileMeta = LocalFileMetaOutter.LocalFileMeta.newBuilder()
                .setChecksum("")
                .setUserMeta(ZeroByteStringHelper.wrap(metaExtension.getMetadataInfo()))
                .setSource(LocalFileMetaOutter.FileSource.FILE_SOURCE_LOCAL)
                .build();

        writer.addFile("", fileMeta);
    }

    public abstract void onSnapshotWrite(SnapshotWriter writer){
        writer.addFile()
    }
}
