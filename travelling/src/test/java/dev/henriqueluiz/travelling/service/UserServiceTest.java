package dev.henriqueluiz.travelling.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import dev.henriqueluiz.travelling.model.AppRole;
import dev.henriqueluiz.travelling.model.AppUser;

@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;

    @Test
    @Sql(
        scripts = { "deleteUser.sql" },
        executionPhase = AFTER_TEST_METHOD
    )
    void givenUserObject_whenCall_thenUserEntityIsExpected() {
        AppUser user = new AppUser();
        user.setFirstName("Henrique");
        user.setLastName("Luiz");
        user.setEmail("test@mail.dev");
        user.setPassword("developer");
        user.setAuthorities(Collections.emptyList());

        AppUser result = userService.saveUser(user);
        assertThat(result.getUserId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @Sql(
        scripts = { "insertUser.sql" },
        executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = { "deleteUser.sql" },
        executionPhase = AFTER_TEST_METHOD
    )
    void givenUserEmail_whenCall_thenUserEntityIsExpected() {
        String email = "test@mail.dev";
        AppUser user = userService.getUserByEmail(email);
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @Sql(
        scripts = { "deleteRole.sql" },
        executionPhase = AFTER_TEST_METHOD
    )
    void givenRoleObject_whenCall_thenRoleEntityIsExpected() {
        String roleName = "test";
        AppRole role = new AppRole();
        role.setName(roleName);
        
        AppRole result = userService.saveRole(role);
        assertThat(result.getRoleId()).isNotNull();
        assertThat(result.getName()).isEqualTo(roleName);
    }

    @Test
    @Sql(
        scripts = { "insertUser.sql", "insertRole.sql" },
        executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
        scripts = { "deleteUser.sql", "deleteRole.sql" },
        executionPhase = AFTER_TEST_METHOD
    )
    void givenRoleNameAndUserEmail_whenCall_thenNoErrorShouldOccur() {
        assertThatCode(
            () -> userService.addRolesToUser("test", "test@mail.dev")
        )
        .doesNotThrowAnyException();
    }
}
