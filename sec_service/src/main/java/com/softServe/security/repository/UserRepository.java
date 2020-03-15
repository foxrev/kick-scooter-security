package com.softServe.security.repository;

import com.softServe.security.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(Integer id);
    boolean existsByEmail(String email);
    @Transactional
    @Modifying
    @Query("update AppUser u set u.isBlocked=true where u.email = ?1")
    int updateBlockedStatus(String email);
}
