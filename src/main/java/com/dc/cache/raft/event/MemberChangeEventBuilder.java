package com.dc.cache.raft.event;


import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.util.Endpoint;
import com.dc.cache.raft.discover.Member;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(staticName = "create")
public  final class MemberChangeEventBuilder {

    private Collection<Member> allMembers;

    private Collection<Member> triggers = Lists.newArrayList();



    public MemberChangeEventBuilder members(Collection<Member> allMembers) {
        this.allMembers = allMembers;
        return this;
    }

    public MemberChangeEventBuilder triggers(Collection<Member> triggers) {
        this.triggers = triggers;
        return this;
    }

    public MemberChangeEventBuilder trigger(Member trigger) {
        this.triggers.add(trigger);
        return this;
    }

    /**
     * build MemberChangeEvent.
     *
     * @return {@link MemberChangedEvent}
     */
    public MemberChangedEvent build() {

        Set<PeerId> allEndpoints = allMembers
                .stream()
                .map(Member::toPeer)
                .collect(Collectors.toSet());

        Set<PeerId> changeEndpoints = triggers
                .stream()
                .map(Member::toPeer)
                .collect(Collectors.toSet());

        return new MemberChangedEvent(allEndpoints, changeEndpoints,null);
    }
}
