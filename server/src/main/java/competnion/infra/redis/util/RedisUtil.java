package competnion.infra.redis.util;


import competnion.global.util.ZonedDateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final ZonedDateTimeUtil dateTimeUtil;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisTemplate<String, LocalDateTime> timeRedisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    // 블랙리스트
    public void set(String key, Object o, int minutes) {
        stringRedisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        stringRedisTemplate.opsForValue().set(key, (String) o, minutes, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.delete(key));
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    public void setBlackList(String key, Object o, long minutes) {
        redisBlackListTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(o.getClass()));
        redisBlackListTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.delete(key));
    }

    public boolean hasKeyBlackList(String key) {
        return Boolean.TRUE.equals(redisBlackListTemplate.hasKey(key));
    }


    // 이메일
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setDataAndExpire(String key, String value, Long duration) {
        if (getData(key) == null) deleteEmailAndCode(key);
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteEmailAndCode(String key) {
        stringRedisTemplate.delete(key);
    }


    // email 전송 api 호출 대기
    public LocalDateTime getRequestTime(String email) {
        return timeRedisTemplate.opsForValue().get(email);
    }

    public void saveRequestTime(String email) {
        ValueOperations<String, LocalDateTime> valueOperations = timeRedisTemplate.opsForValue();
        valueOperations.set(email, LocalDateTime.now(), 5L, SECONDS);
    }

    public boolean checkRequestAllowed(String email) {
        LocalDateTime requestTime = getRequestTime(email);
        return requestTime == null;
    }
}
