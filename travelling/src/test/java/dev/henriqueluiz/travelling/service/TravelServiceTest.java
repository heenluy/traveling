package dev.henriqueluiz.travelling.service;

import dev.henriqueluiz.travelling.model.Travel;
import dev.henriqueluiz.travelling.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
class TravelServiceTest {

    @Autowired
    TravelService service;

    @Autowired
    UserRepo userRepo;

    @Test
    @Sql(
            scripts = { "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenEntity_whenCall_thenTravelObjectIsExpected() {
        Travel entity = new Travel(
                null,
                "São Paulo",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                BigDecimal.valueOf(3000L),
                userRepo.findById(1L).orElseThrow()
        );
        Travel savedEntity = service.saveTravel(entity);
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getTravelId()).isNotNull();
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertTravel.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenIdAndEntity_whenCall_thenTravelObjectIsExpected() {
        Travel entity = new Travel(
                null,
                "São Paulo",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                BigDecimal.valueOf(3000L),
                userRepo.findById(1L).orElseThrow()
        );
        Travel updatedEntity = service.updateTravel(1L, entity);
        assertThat(updatedEntity).isNotNull();
        assertThat(updatedEntity.getTravelId()).isNotNull();
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertTravel.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenIdAndUserEmail_whenCall_thenNoErrorShouldOccur() {
        assertThatCode(() -> service.deleteTravel(1L, "test@mail.dev"))
                .doesNotThrowAnyException();
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertTravel.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenIdAndUserEmail_whenCall_thenTravelObjectIsExpected() {
        Travel foundEntity = service.getById(1L, "test@mail.dev");
        assertThat(foundEntity).isNotNull();
        assertThat(foundEntity.getTravelId()).isNotNull();
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertTravel.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUserEmail_whenCall_thenNotEmptyListIsExpected() {
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        Page<Travel> entities = service.getAllByUser("test@mail.dev", pageable);
        assertThat(entities).isNotEmpty();
        assertThat(entities.getTotalElements()).isGreaterThan(0);
    }
}