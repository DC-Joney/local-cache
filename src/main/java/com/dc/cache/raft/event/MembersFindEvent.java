package com.dc.cache.raft.event;

import com.alipay.sofa.jraft.entity.PeerId;
import com.dc.cache.raft.discover.Member;
import com.turing.common.notify.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@ToString
@RequiredArgsConstructor(staticName = "create")
public class MembersFindEvent extends Event {

    /**
     * 所有的Member节点
     */
    private final Set<Member> members;


    public Set<PeerId> getPeers() {
        return members.stream()
                .map(member -> member.toPeer())
                .collect(Collectors.toSet());
    }

}
