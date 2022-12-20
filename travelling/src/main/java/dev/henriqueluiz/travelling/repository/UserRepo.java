package dev.henriqueluiz.travelling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.henriqueluiz.travelling.model.AppUser;

public interface UserRepo extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
