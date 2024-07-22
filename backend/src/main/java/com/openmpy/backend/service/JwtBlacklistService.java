package com.openmpy.backend.service;

import com.openmpy.backend.entity.JwtBlacklist;
import com.openmpy.backend.jwt.JwtUtil;
import com.openmpy.backend.repository.JwtBlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JwtBlacklistService {

    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final JwtUtil jwtUtil;

    public void blacklistToken(String token, LocalDateTime expirationTime, String username) {
        JwtBlacklist jwtBlacklist = new JwtBlacklist();
        jwtBlacklist.setToken(token);
        jwtBlacklist.setExpirationTime(expirationTime);
        jwtBlacklist.setUsername(username);
        jwtBlacklistRepository.save(jwtBlacklist);
    }

    public boolean isTokenBlacklisted(String currentToken) {
        String username = jwtUtil.getUsernameFromToken(currentToken);
        Optional<JwtBlacklist> blacklistedToken = jwtBlacklistRepository.findTopByUsernameOrderByExpirationTimeDesc(username);
        if (blacklistedToken.isEmpty()) {
            return false;
        }

        Instant instant = jwtUtil.getExpirationDateFromToken(currentToken).toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return blacklistedToken.get().getExpirationTime().isAfter(localDateTime.minusMinutes(60));
    }
}
