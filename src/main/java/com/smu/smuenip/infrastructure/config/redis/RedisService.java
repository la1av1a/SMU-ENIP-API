package com.smu.smuenip.infrastructure.config.redis;


import com.smu.smuenip.domain.user.model.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RedisService {

    private final RedisTemplate<String, User> redisTemplate;
    private final ZSetOperations<String, User> zSetOperations;

    public RedisService(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
    }

    public void saveOrUpdateUser(User user) {
        zSetOperations.add("userScore", user, user.getScore());
    }

    public void flushAll() {
        redisTemplate.delete("userScore");
    }

    public Set<User> getUsers() {
        return zSetOperations.reverseRange("userScore", 0, -1);
    }
}