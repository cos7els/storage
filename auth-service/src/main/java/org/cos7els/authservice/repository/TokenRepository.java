package org.cos7els.authservice.repository;

import org.cos7els.authservice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Query("SELECT t FROM Token t WHERE t.userId = :userId AND (t.expired = false AND t.revoked = false)")
    List<Token> findAllValidTokensByUser(@Param("userId") Long userId);
}