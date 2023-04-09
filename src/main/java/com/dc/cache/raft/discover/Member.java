package com.dc.cache.raft.discover;

import com.alipay.sofa.jraft.JRaftUtils;
import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.util.Endpoint;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

@Getter
@Setter
public class Member {

    /**
     * 最后上报心跳的时间
     */
    public static final String LAST_REFRESH_TIME = "last_refresh_time";

    /**
     * 节点ip + port
     */
    private Endpoint endpoint;


    private NodeStatus status = NodeStatus.DOWN;

    /**
     * 节点扩展信息
     */
    private Map<String, Object> extendInfo = new ConcurrentSkipListMap<>();


    public Member(String address) {
        this.endpoint = JRaftUtils.getEndPoint(address);
    }

    public Member addExtended(String key, Object value){
        extendInfo.put(key, value);
        return this;
    }


    public String getAddress(){
        return endpoint.toString();
    }

    public Endpoint toEndpoint() {
        return endpoint;
    }

    public PeerId toPeer() {
        return JRaftUtils.getPeerId(endpoint.toString());
    }

    public void copyFrom(Member newMember) {
        this.setEndpoint(newMember.getEndpoint());
        this.setStatus(newMember.getStatus());
        this.setExtendInfo(newMember.getExtendInfo());
    }

    public enum NodeStatus {
        /**
         * 节点正常
         */
        UP,

        /**
         * 节点宕机
         */
        DOWN,

        /**
         * 未知状况
         */
        UNKNOWN ;


        public boolean isDown() {
            return this == NodeStatus.DOWN;
        }

    }
}
