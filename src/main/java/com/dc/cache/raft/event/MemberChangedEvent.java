package com.dc.cache.raft.event;

import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.util.Endpoint;
import com.turing.common.notify.Event;
import lombok.Getter;

import java.util.Set;

@Getter
public class MemberChangedEvent extends Event{

    /**
     * 需要被删除的节点
     */
    private final Set<PeerId> removeEndpoints;

    /**
     * 需要添加的节点
     */
    private final Set<PeerId> addEndpoints;


    private final Set<PeerId> serverList;


    public MemberChangedEvent(Set<PeerId> removeEndpoints,
                              Set<PeerId> addEndpoints,
                              Set<PeerId> serverList) {
        this.removeEndpoints = removeEndpoints;
        this.addEndpoints = addEndpoints;
        this.serverList = serverList;

    }
}
