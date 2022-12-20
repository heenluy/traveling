package dev.henriqueluiz.travelling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.henriqueluiz.travelling.model.RefreshToken;


public interface TokenRepo extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
}
