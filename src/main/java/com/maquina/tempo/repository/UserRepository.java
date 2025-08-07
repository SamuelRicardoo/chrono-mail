package com.maquina.tempo.repository;

import com.maquina.tempo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT v.user FROM VerificationToken v WHERE v.token = :tokenU")
    Optional<User> findUserByToken(UUID tokenU);
}