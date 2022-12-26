package dev.henriqueluiz.travelling.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.henriqueluiz.travelling.exception.entity.RoleNotFoundException;

@SpringBootTest
public class UserServiceFailuresTest {
    
    @Autowired
    private UserService service;

    @Test
    void givenInvalidRoleName_whenThrown_thenRoleNotFoundExceptionIsExpected() {
        Assertions.assertThatExceptionOfType(RoleNotFoundException.class)
            .isThrownBy(() -> service.addRolesToUser("dummy", "test@mail.dev"))
            .withMessage("Role not found: 'dummy'");
    }

    @Test
    void givenInvalidUserEmail_whenThrown_thenUsernameNotFoundExceptionIsExpected() {
        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
            .isThrownBy(() -> service.getUserByEmail("test@not.found"))
            .withMessage("User not found: 'test@not.found'");
    }
}
