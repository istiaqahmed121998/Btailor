package com.backend.userauthserivce.domain.auth;

import com.backend.userauthserivce.domain.user.UserModel;
import com.backend.userauthserivce.domain.user.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class PasswordResetHelper {
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    public PasswordResetHelper(UserRepository userRepository, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public UserModel processForgotPassword(String email,String otpCode) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            validateRetryLimit(email); // throttle

            redisTemplate.opsForValue().set("OTP_" + email, otpCode, Duration.ofMinutes(5));

            return user;
        }
        return null;
    }

    public void validateRetryLimit(String email) {
        String retryKey = "OTP_RETRY_" + email;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String countStr = ops.get(retryKey);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;

        if (count >= 5) {
            throw new IllegalStateException("Too many OTP requests. Try again in an hour.");
        }

        ops.increment(retryKey);
        if (count == 0) {
            redisTemplate.expire(retryKey, Duration.ofHours(1));
        }
    }

    public boolean verifyOtp(String email, String inputOtp) {
        String key = "OTP_" + email;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        String storedOtp = ops.get(key);
        if (storedOtp == null) return false;

        boolean matched = storedOtp.equals(inputOtp);

        // Invalidate OTP after verification attempt (one-time use)
        redisTemplate.delete(key);

        return matched;
    }
}
