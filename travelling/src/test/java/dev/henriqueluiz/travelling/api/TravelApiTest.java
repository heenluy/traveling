package dev.henriqueluiz.travelling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.henriqueluiz.travelling.model.mapper.TravelDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TravelApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    Jwt jwt;
    TravelDto entity;
    
    @BeforeEach
    void setup() {
        Instant now = Instant.now();
        String subject = "test@mail.dev";
        jwt = Jwt.withTokenValue(UUID.randomUUID().toString())
                .header("alg", "RS256")
                .claim("iat", now)
                .claim("exp", now.plus(5, ChronoUnit.MINUTES))
                .claim("sub", subject)
                .claim("scope", "write read")
                .build();

        entity = new TravelDto(
                "São Paulo",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3),
                BigDecimal.valueOf(3000L)
        );
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteTravel.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenRequestEntity_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        String request = mapper.writeValueAsString(entity);
        ResultActions result = mvc.perform(
                post("/travels/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(jwt().jwt(jwt))
                        .content(request));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.destination").value(entity.destination()));
        result.andExpect(jsonPath("$.budget").value(entity.budget()));
        result.andDo(print());
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
    void givenIdAndRequestEntity_whenCall_thenAcceptedResponseStatusIsExpected() throws Exception {
        String request = mapper.writeValueAsString(entity);
        ResultActions result = mvc.perform(
                put("/travels/update")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(jwt().jwt(jwt))
                        .param("id", String.valueOf(1))
                        .content(request));

        result.andExpect(status().isAccepted());
        result.andExpect(jsonPath("$.destination").value(entity.destination()));
        result.andExpect(jsonPath("$.budget").value(entity.budget()));
        result.andDo(print());
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
    void givenId_whenCall_thenAcceptedResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                delete("/travels/delete")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(jwt().jwt(jwt))
                        .param("id", String.valueOf(1)));
        
        result.andExpect(status().isAccepted());
        result.andDo(print());
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
    void givenId_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/travels/get")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(jwt().jwt(jwt))
                        .param("id", String.valueOf(1)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.destination").exists());
        result.andExpect(jsonPath("$.budget").exists());
        result.andDo(print());
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
    void givenPageValues_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/travels/get/all")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(jwt().jwt(jwt))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(1)));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.content[0]").isNotEmpty());
        result.andExpect(jsonPath("$.content[0].destination").exists());
        result.andExpect(jsonPath("$.content[0].budget").exists());
        result.andDo(print());
    }
}