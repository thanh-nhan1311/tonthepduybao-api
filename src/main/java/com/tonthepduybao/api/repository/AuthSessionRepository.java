package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * AuthSessionRepository
 *
 * @author kha
 * @since 2025/07/01
 */
public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {

    Optional<AuthSession> findByToken(String token);

    Optional<AuthSession> findByUser_Username(String username);

}
