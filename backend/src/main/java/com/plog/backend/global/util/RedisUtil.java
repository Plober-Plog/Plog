package com.plog.backend.global.util;

import com.plog.backend.global.exception.NotValidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisUtil {
    private final StringRedisTemplate template;

    public String getData(String key) {
        if (key == null) {
            throw new NotValidRequestException("RedisUtil : 키는 반드시 값이 존재 해야 합니다.");
        }
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existData(String key) {
        if (key == null) {
            throw new NotValidRequestException("RedisUtil : 키는 반드시 값이 존재 해야 합니다.");
        }
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String value, long duration) {
        if (key == null) {
            throw new NotValidRequestException("RedisUtil : 키는 반드시 값이 존재 해야 합니다.");
        }
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        if (key == null) {
            throw new NotValidRequestException("RedisUtil : 키는 반드시 값이 존재 해야 합니다.");
        }
        template.delete(key);
    }
}
