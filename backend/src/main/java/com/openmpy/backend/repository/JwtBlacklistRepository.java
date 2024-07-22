package com.openmpy.backend.repository;

import com.openmpy.backend.entity.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {

    Optional<JwtBlacklist> findByToken(String token);

    Optional<JwtBlacklist> findTopByUsernameOrderByExpirationTimeDesc(String token);
}
