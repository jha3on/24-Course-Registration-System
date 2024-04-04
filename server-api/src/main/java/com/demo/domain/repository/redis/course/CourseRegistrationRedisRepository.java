package com.demo.domain.repository.redis.course;

import com.demo.global.common.exception.ApiException;
import com.demo.global.common.exception.enums.ApiExceptionType;
import com.demo.global.configuration.redis.enums.RedisScriptType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseRegistrationRedisRepository {
    private final RedisTemplate<String, String> redisQueryFactory;
    private final RedisScript<Boolean> redisScriptOfCourseSeatCountPlus;
    private final RedisScript<Boolean> redisScriptOfCourseSeatCountMinus;

    /**
     * 커넥션 상태를 확인한다.
     */
    public Boolean isShutdown() {
        return redisQueryFactory.execute(RedisConnection::isClosed);
    }

    /**
     * 데이터 Key 값을 확인한다. (hasKey(K))
     */
    public Boolean hasKeyBy(String key) {
        return redisQueryFactory.hasKey(key);
    }

    /**
     * 데이터를 생성한다. (ValueOperations.set(K, V))
     */
    public void setByValueOps(String key, String value) {
        // if (Objects.equals(redisQueryFactory.hasKey(key), Boolean.TRUE)) {
        //     throw ApiException.of409(ApiExceptionType.REDIS_KEY_EXISTED);
        // }

        redisQueryFactory.opsForValue().set(key, value);
    }

    /**
     * 데이터를 수정한다. (ValueOperations.set(K, V))
     */
    public void resetByValueOps(String key, String value) {
        // if (Objects.equals(redisQueryFactory.hasKey(key), Boolean.FALSE)) {
        //     throw ApiException.of409(ApiExceptionType.REDIS_KEY_NOT_EXISTED);
        // }

        redisQueryFactory.opsForValue().set(key, value);
    }

    /**
     * 데이터를 조회한다. (ValueOperations.get(K))
     */
    public String getByValueOps(String key) {
        return redisQueryFactory.opsForValue().get(key);
    }

    /**
     * 데이터를 생성한다. (ZSetOperations.add(K, V, score))
     */
    public Boolean setByZSetOps(String key, String value) {
        return redisQueryFactory.opsForZSet().add(key, value, System.currentTimeMillis());
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

    /**
     * 스크립트를 실행한다.
     */
    public Boolean executeByScript(RedisScriptType type, String key, String value) {
        if (type.equals(RedisScriptType.COURSE_SEAT_COUNT_PLUS)) {
            return redisQueryFactory.execute(redisScriptOfCourseSeatCountPlus, Collections.singletonList(key), value);
        } else if (type.equals(RedisScriptType.COURSE_SEAT_COUNT_MINUS)) {
            return redisQueryFactory.execute(redisScriptOfCourseSeatCountMinus, Collections.singletonList(key), value);
        } else {
            throw ApiException.of500(ApiExceptionType.COMMON_QUERY_SCRIPT_NOT_SUPPORTED);
        }
    }
}