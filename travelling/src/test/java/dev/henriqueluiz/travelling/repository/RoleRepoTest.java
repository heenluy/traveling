package dev.henriqueluiz.travelling.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import dev.henriqueluiz.travelling.model.AppRole;

@SpringBootTest
public class RoleRepoTest {
    
    @Autowired
    private RoleRepo repository;

    @Test
    @Sql(scripts = "insertRole.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "deleteRole.sql", executionPhase = AFTER_TEST_METHOD)
    void givenRoleName_whenRun_thenRoleEntityIsExpected() {
        AppRole role = repository.findByName("test").orElseThrow();
        assertThat(role.getRoleId()).isNotNull();
        assertThat(role.getName()).isEqualTo("test");
    }
}
