package com.dc.cache.raft.discover;

import java.util.List;
import java.util.Set;

public interface MemberDiscover {

    /**
     * 获取节点列表
     */
    Set<Member> getMembers();
}
