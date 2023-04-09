package com.dc.cache.raft.snapshot;

import com.turing.common.serilizer.ProtoStuffUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.springframework.util.ObjectUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@NotThreadSafe
public class DirectMap {

    private volatile ByteBuf metadataStore;

    private Map<String, Integer> indexMap = new HashMap<>(16);


    public DirectMap() {
        this.metadataStore = PooledByteBufAllocator.DEFAULT.directBuffer(1 << 8);
    }

    private DirectMap(Map<String, Integer> indexMap, ByteBuf metadataStore) {
        this.indexMap = indexMap;
        this.metadataStore = metadataStore;
    }

    public void putValue(String metaKey, Object value) {

        if (ObjectUtils.isEmpty(metaKey)) {
            throw new IllegalArgumentException("Meta key must not be empty");
        }

        Integer index = indexMap.get(metaKey);
        boolean mark = true;
        if (index == null) {
            index = metadataStore.writerIndex();
            mark = false;
        }

        if (mark) {
            metadataStore.markWriterIndex();
        }

        metadataStore.markWriterIndex();
        byte[] metaValue = ProtoStuffUtils.serialize(value);
        //将key数据写入到堆外内存
        metadataStore.writerIndex(index)
                .writeInt(metaKey.length())
                .writeCharSequence(metaKey, Charset.defaultCharset());

        //将value写入到堆外内存
        metadataStore.writeInt(metaValue.length)
                .writeBytes(metaValue);

        indexMap.put(metaKey, index);
        if (mark)
            metadataStore.resetWriterIndex();
    }

    public <T> T remove(String metaKey, Class<T> valueClass) {
        Integer index = indexMap.remove(metaKey);
        T metaValue = getValue(index, valueClass);
        if (index != null) {
            metadataStore.markReaderIndex();
            metadataStore.readerIndex(index);
            int length = metadataStore.readInt();
            ByteBuf prevBuf = metadataStore.slice(0, index);
            //获取key的长度
            int keyLength = metadataStore.readerIndex(index)
                    .readInt();
            //获取value的长度
            int valueLength = metadataStore.skipBytes(keyLength)
                    .readInt();

            ByteBuf postBuf = metadataStore.slice(index + 4 + keyLength + 4 + valueLength, metadataStore.writerIndex());
            ByteBuf newMetaStore = PooledByteBufAllocator.DEFAULT.directBuffer(postBuf.readableBytes() + postBuf.readableBytes());
            newMetaStore.writeBytes(prevBuf).writeBytes(postBuf);

            ByteBuf oldMetaStore = metadataStore;
            this.metadataStore = newMetaStore;

            //将老的内存释放掉
            oldMetaStore.release();
        }

        return metaValue;
    }


    public <T> T getValue(String metaKey, Class<T> valueClass) {
        Integer index = indexMap.get(metaKey);
        return getValue(index, valueClass);
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(Integer index, Class<T> valueClass) {
        if (index == null)
            return null;

        metadataStore.markReaderIndex();
        metadataStore.readerIndex(index);
        int keyLength = metadataStore.readInt();
        int valueLength = metadataStore.skipBytes(keyLength).readInt();
        byte[] metaValue = new byte[valueLength];
        metadataStore.readBytes(metaValue);
        metadataStore.resetReaderIndex();
        Object deserialize = ProtoStuffUtils.deserialize(metaValue);

        if (valueClass.isAssignableFrom(deserialize.getClass())) {
            return (T) deserialize;
        }

        return null;
    }

    public int size() {
        return indexMap.size();
    }

    public void clear() {
        indexMap.clear();
        metadataStore.release();
    }

    public Set<String> keySet() {
        return indexMap.keySet();
    }


    /**
     * 获取文件的元数据信息
     */
    public ByteBuffer getMetadataInfo() {
        return metadataStore.nioBuffer().asReadOnlyBuffer();
    }

    /**
     * 获取文件的元数据信息
     */
    public static DirectMap from(ByteBuffer buffer) {
        ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(buffer.capacity());
        byteBuf.writeBytes(buffer);

        //help gc
        buffer = null;

        ByteBuf slice = byteBuf.slice();
        int capacity = slice.capacity();
        int readLength = 0;
        Map<String, Integer> indexMap = new HashMap<>();

        while (readLength < capacity) {
            int index = slice.readerIndex();
            int keyLength = slice.readInt();
            //获取index key
            String metaKey = (String) slice.readBytes(keyLength)
                    .readCharSequence(keyLength, Charset.defaultCharset());
            //获取 value占用的字节数
            int valueLength = slice.readInt();
            slice.skipBytes(valueLength);
            indexMap.put(metaKey, index);
            readLength += 4 + keyLength + 4 + valueLength;
        }

        return new DirectMap(indexMap, byteBuf);
    }

}
