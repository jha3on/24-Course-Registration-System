package com.demo.domain.repository.redis.course;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CourseRegistrationRedisRepository {
    private final RedisTemplate<String, String> redisQueryFactory;

    /**
     * 커넥션 상태를 확인한다.
     */
    public Boolean isShutdown() {
        return redisQueryFactory.execute(RedisConnection::isClosed);
    }

    /**
     * 데이터를 순위 조회한다. (ZSetOperations.rank(K, V))
     */
    public Long getRankByZSetOps(String key, String value) {
        return redisQueryFactory.opsForZSet().rank(key, value);
    }

    /**
     * 데이터를 범위 조회한다. (ZSetOperations.range(K, start, end))
     */
    public Set<String> getRangeByZSetOps(String key, Long rangeStart, Long rangeStop) {
        return redisQueryFactory.opsForZSet().range(key, rangeStart, rangeStop);
    }

    /**
     * 데이터를 삭제한다. (ZSetOperations.remove(K, V))
     */
    public Long deleteByZSetOps(String key, String value) {
        return redisQueryFactory.opsForZSet().remove(key, value);
    }
}