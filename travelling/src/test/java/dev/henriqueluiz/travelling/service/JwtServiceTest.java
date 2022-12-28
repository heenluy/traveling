package dev.henriqueluiz.travelling.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    JwtEncoder encoder;

    @Autowired
    JwtDecoder decoder;

    @Autowired
    JwtService jwtService;

    final String USERNAME = "test@mail.dev";
    final String PASSWORD = "developer";

    @Test
    @WithMockUser(value = USERNAME, password = PASSWORD)
    void givenAuthentication_whenCall_thenTokensAreExpected() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, String> tokens = jwtService.getAccessToken(authentication);
        String subject = decoder.decode(tokens.get("access_token")).getSubject();

        assertThat(tokens).isNotNull();
        assertThat(tokens.containsKey("access_token")).isEqualTo(true);
        assertThat(tokens.containsKey("refresh_token")).isEqualTo(true);
        assertThat(subject).isEqualTo(USERNAME);
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
    void givenRefreshToken_whenCall_thenNewTokensAreExpected() {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(USERNAME)
                .claim("scope", Collections.emptyList())
                .build();

        String refreshToken = encoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        Map<String, String> tokens = jwtService.refreshToken(refreshToken);
        String subject = decoder.decode(tokens.get("access_token")).getSubject();

        assertThat(tokens).isNotNull();
        assertThat(tokens.containsKey("access_token")).isEqualTo(true);
        assertThat(tokens.containsKey("refresh_token")).isEqualTo(true);
        assertThat(subject).isEqualTo(USERNAME);
    }
}