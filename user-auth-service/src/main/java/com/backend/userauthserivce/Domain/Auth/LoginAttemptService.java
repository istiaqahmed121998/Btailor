package com.backend.userauthserivce.Domain.Auth;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final StringRedisTemplate redisTemplate;

    private static final String FAILED_ATTEMPTS_PREFIX = "failed_attempts:";
    private static final String ACCOUNT_LOCK_PREFIX = "account_lock:";
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_TIME_IN_MINUTES = 30;

    public LoginAttemptService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void incrementFailedAttempts(String email) {
        String key = FAILED_ATTEMPTS_PREFIX + email;
        String lockKey = ACCOUNT_LOCK_PREFIX + email;

        // Increment failed attempts counter
        Long attempts = redisTemplate.opsForValue().increment(key, 1);
        if (attempts != null && attempts >= MAX_FAILED_ATTEMPTS) {
            // Lock the account if failed attempts exceed the threshold
            redisTemplate.opsForValue().set(lockKey, LocalDateTime.now().toString(), LOCK_TIME_IN_MINUTES, TimeUnit.MINUTES);
        }
    }

    public boolean isAccountLocked(String email) {
        String lockKey = ACCOUNT_LOCK_PREFIX + email;
        String lockTime = redisTemplate.opsForValue().get(lockKey);

        if (lockTime != null) {
            // Check if lock time has expired
            LocalDateTime lockDateTime = LocalDateTime.parse(lockTime);
            if (lockDateTime.plusMinutes(LOCK_TIME_IN_MINUTES).isBefore(LocalDateTime.now())) {
                // Lock has expired, reset failed attempts and unlock
                redisTemplate.delete(lockKey);
                redisTemplate.delete(FAILED_ATTEMPTS_PREFIX + email);
                return false;
            }
            return true;
        }
        return false;
    }

    public void resetFailedAttempts(String email) {
        redisTemplate.delete(FAILED_ATTEMPTS_PREFIX + email);
    }
}
