package com.dc.cache.raft.discover;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class YamlMemberDiscover implements MemberDiscover{

    private static final String CONFIG_NAME = "cluster.yml";
    private static final String CONFIG_PREFIX = "cache.cluster.nodes";

    @Autowired
    private SpringResourceLoader resourceLoader;

    @Override
    public Set<Member> getMembers() {
        BindResult<ServerList> bindResult = resourceLoader.bindProperty(CONFIG_NAME, CONFIG_PREFIX, ServerList::new);
        return bindResult.map(ServerList::getMembers)
                .orElse(Collections.emptySet());
    }

    @Getter
    @Setter
    @ToString
    public static class ServerList {

        private Set<String> nodes;

        public Set<Member> getMembers(){
            return nodes.stream()
                    .map(Member::new)
                    .collect(Collectors.toSet());
        }
    }
}
