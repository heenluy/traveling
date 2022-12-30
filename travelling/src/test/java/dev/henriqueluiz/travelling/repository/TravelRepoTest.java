package dev.henriqueluiz.travelling.repository;

import dev.henriqueluiz.travelling.model.Travel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
class TravelRepoTest {

    @Autowired
    TravelRepo repository;

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertTravel.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenIdAndUserEmail_whenRun_thenTravelObjectIsExpected() {
        Travel entity = repository.findByUser(1L, "test@mail.dev").orElseThrow();
        assertThat(entity).isNotNull();
        assertThat(entity.getTravelId()).isNotNull();
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
    void givenUserEmail_whenRun_thenNotEmptyListIsExpected() {
        Pageable pageable = Pageable.ofSize(3).withPage(0);
        Page<Travel> entities = repository.findAllByUser("test@mail.dev", pageable);
        assertThat(entities).isNotEmpty();
        assertThat(entities.getTotalElements()).isGreaterThan(0);
    }
}