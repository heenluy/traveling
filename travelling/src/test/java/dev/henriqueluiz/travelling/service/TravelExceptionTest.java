package dev.henriqueluiz.travelling.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.travelling.model.Travel;
import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static java.math.BigDecimal.ZERO;

@SpringBootTest
public class TravelExceptionTest {

    @Autowired
    TravelService service;

    @Test
    void givenEntityWithoutUser_whenThrow_thenIllegalStateExceptionIsExpected() {
        Travel entity = new Travel(1L, "New York", LocalDate.now().plusDays(1), LocalDate.now().plusDays(2), ZERO, null);
        Assertions.assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> service.saveTravel(entity))
                .withMessage("User cannot be null");
    }

    @Test
    void givenNonExistentId_whenThrow_thenEntityNotFoundExceptionIsExpected() {
        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> service.deleteTravel(1L, null))
                .withMessage("Entity not found: 'id 1'");
    }
}
