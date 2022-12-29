package dev.henriqueluiz.travelling.repository;

import dev.henriqueluiz.travelling.model.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TravelRepo extends JpaRepository<Travel, Long> {
    @Query(
            "SELECT t FROM Travel t JOIN AppUser u " +
                    "ON t.travelId = ?1 AND u.email = ?2"
    )
    Optional<Travel> findByUser(Long travelId, String email);

    @Query("SELECT t FROM Travel t WHERE t.user.email = ?1")
    Page<Travel> findAllByUser(String email, Pageable pageable);
}