package com.market.ecommerce.common.client.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.market.ecommerce.common.exception.redis.RedisCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.Objects;

import static com.market.ecommerce.common.exception.redis.RedisErrorCode.JSON_DESERIALIZE_FAILED;
import static com.market.ecommerce.common.exception.redis.RedisErrorCode.JSON_SERIALIZE_FAILED;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisClient {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public Long incrementValueWithExpireOnce(String key, long ttl) {
        Boolean exists = redisTemplate.hasKey(key);

        Long value = redisTemplate.opsForValue().increment(key);

        if (Boolean.FALSE.equals(exists)) {
            redisTemplate.expire(key, Duration.ofSeconds(ttl));
        }

        return value;
    }

    public <T> void putValue(Object key, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key.toString(), json);
        } catch (JsonProcessingException e) {
            throw new RedisCustomException(JSON_SERIALIZE_FAILED);
        }
    }

    public <T> T getValue(Object key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key.toString());
        if (ObjectUtils.isEmpty(value)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(value.toString(), clazz);
            } catch (JsonProcessingException e) {
                throw new RedisCustomException(JSON_DESERIALIZE_FAILED);
            }
        }
    }

    public void deleteValue(Object key) {
        redisTemplate.delete(key.toString());
    }

    public <T> void putHash(String hashKey, Object fieldKey, T value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForHash().put(hashKey, fieldKey.toString(), json);
        } catch (JsonProcessingException e) {
            throw new RedisCustomException(JSON_SERIALIZE_FAILED);
        }
    }

    public <T> T getHash(String hashKey, Object fieldKey, Class<T> clazz) {
        Object value = redisTemplate.opsForHash().get(hashKey, fieldKey.toString());
        if (ObjectUtils.isEmpty(value)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(value.toString(), clazz);
            } catch (JsonProcessingException e) {
                throw new RedisCustomException(JSON_DESERIALIZE_FAILED);
            }
        }
    }

    public void deleteHash(String hashKey, Object fieldKey) {
        redisTemplate.opsForHash().delete(hashKey, fieldKey.toString());
    }
}