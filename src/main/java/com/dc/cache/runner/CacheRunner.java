package com.dc.cache.runner;

import com.alipay.sofa.jraft.option.NodeOptions;
import com.dc.cache.raft.RaftServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CacheRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        NodeOptions nodeOptions = new NodeOptions();
        RaftServer raftServer = new RaftServer(nodeOptions);
        raftServer.afterPropertiesSet();
    }
}
