package com.dc.cache.raft;

import com.google.protobuf.Message;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ProtoUtils {

    /**
     * 添加版本号
     *
     * @param flag    flag
     * @param version 版本号
     */
    public int addVersion(int flag, int version) {
        return flag & (-1 << 3) | version;
    }

    /**
     * 添加类型， 最多支持128 种类型，一般也不会需要这么多
     *
     * @param flag flag
     * @param type 传输的类型
     */
    public int addType(int flag, int type) {
        return flag & ~(-1 << 3) | type << 3;
    }

    /**
     * 获取对应的类型
     *
     * @param flag flag
     */
    public int getType(int flag) {
        return flag >> 3 & ~(-1 << 16);
    }

    /**
     * 判断当前Message 是否是可读的
     * @param message message
     */
    public static boolean isRead(Message message) {
        int type = MarshallerRegistryCache.findType(message);
        return (type & 1) == 0;
    }


}
