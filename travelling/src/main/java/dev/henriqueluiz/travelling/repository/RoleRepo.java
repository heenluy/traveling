package dev.henriqueluiz.travelling.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.henriqueluiz.travelling.model.AppRole;

public interface RoleRepo extends JpaRepository<AppRole, Long> {
    Optional<AppRole> findByName(String name);
}
