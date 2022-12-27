package dev.henriqueluiz.travelling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.henriqueluiz.travelling.model.AppRole;
import dev.henriqueluiz.travelling.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserApiTest {
    
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    @Sql(
        scripts = { "deleteUser.sql" },
        executionPhase = AFTER_TEST_METHOD
    )
    void givenUserRequest_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        AppUser user = new AppUser();
        user.setFirstName("Henrique");
        user.setLastName("Luiz");
        user.setEmail("test@mail.dev");
        user.setPassword("developer");
        user.setAuthorities(Collections.emptyList());
        String request = mapper.writeValueAsString(user);

        ResultActions result = mvc.perform(
            post("/users/save")
                .contentType(APPLICATION_JSON_VALUE)
                .accept(APPLICATION_JSON)
                .content(request));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.firstName").value(user.getFirstName()));
        result.andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @Sql(
            scripts = { "deleteRole.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenRoleObject_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        AppRole role = new AppRole(null, "test");
        String request = mapper.writeValueAsString(role);

        ResultActions result = mvc.perform(
                post("/roles/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .content(request));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.name").value(role.getName()));
    }

    @Test
    @Sql(
            scripts = { "insertRole.sql", "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteRole.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenRoleNameAndUserEmail_whenCall_thenAcceptedResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                put("/roles/add-to-user")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .param("role", "test")
                        .param("email", "test@mail.dev"));

        result.andExpect(status().isAccepted());
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
    void givenUserEmail_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        String firstName = "Henrique";
        String email = "test@mail.dev";
        ResultActions result = mvc.perform(
                get("/users/get-by-email")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .param("email", email)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_manager"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.firstName").value(firstName));
        result.andExpect(jsonPath("$.email").value(email));
    }
}
