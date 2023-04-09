package com.dc.cache.raft;

import com.dc.cache.raft.discover.Member;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberUtil {

    /**
     * Judge whether basic info has changed.
     *
     * @param actual   actual member
     * @param expected expected member
     * @return true if one content is different, otherwise false
     */
    public static boolean isBasicInfoChanged(Member actual, Member expected) {
        if (null == expected) {
            return null != actual;
        }
        if (!expected.getEndpoint().equals(actual.getEndpoint())) {
            return true;
        }

        if (!expected.getStatus().equals(actual.getStatus())) {
            return true;
        }

        return false;
    }
}
