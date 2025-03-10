package com.multitenant.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multitenant.auth.entity.SessionEntity;
import com.multitenant.auth.entity.UserEntity;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    
    List<SessionEntity> findByUser(UserEntity user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
