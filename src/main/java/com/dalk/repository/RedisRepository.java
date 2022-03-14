package com.dalk.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@RequiredArgsConstructor
@Repository
@Slf4j
public class RedisRepository {

    public static final String ENTER_INFO = "ENTER_INFO";

    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> stringHashOpsEnterInfo;

    public void setSessionRoomId(String sessionId, String roomId) {
        stringHashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId);
    }

    public String getSessionRoomId(String sessionId) {
        return stringHashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // sessionId 삭제
    public void removeUserEnterInfo(String sessionId) {
        stringHashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }
}
