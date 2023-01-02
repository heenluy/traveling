package dev.henriqueluiz.travelling.service;

import dev.henriqueluiz.travelling.exception.entity.InvalidTokenException;
import dev.henriqueluiz.travelling.model.AppUser;
import dev.henriqueluiz.travelling.model.mapper.TokenResponse;
import dev.henriqueluiz.travelling.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final static Logger LOG = LoggerFactory.getLogger(JwtService.class);

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;
    private final UserRepo userRepo;

    public TokenResponse getAccessToken(Authentication authentication) {
        LOG.debug("Generating tokens to: '{}'", authentication.getName());
        Instant now = Instant.now();       
        List<String> scope = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        
        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(25, MINUTES))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();

        JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, DAYS))
            .subject(authentication.getName())
            .claim("scope", Collections.emptyList())
            .build();

        String accessToken = this.encoder
            .encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
        
        String refreshToken = this.encoder
            .encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();

        return buildTokenResponse(
                accessToken,
                refreshToken,
                accessClaims.getExpiresAt(),
                refreshClaims.getExpiresAt()
        );
    }

    public TokenResponse refreshToken(String token) {
        AppUser user = getUserByToken(token);
        LOG.debug("Refreshing tokens to: '{}'", user.getEmail());
        
        Instant now = Instant.now();
        List<String> scope = user.getAuthorities().stream().toList();

        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(25, MINUTES))
            .subject(user.getEmail())
            .claim("scope", scope)
            .build();

        JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, DAYS))
            .subject(user.getEmail())
            .claim("scope", Collections.emptyList())
            .build();

        String accessToken = this.encoder
            .encode(JwtEncoderParameters.from(accessClaims)).getTokenValue();
        
        String refreshToken = this.encoder
            .encode(JwtEncoderParameters.from(refreshClaims)).getTokenValue();

        return buildTokenResponse(
                accessToken,
                refreshToken,
                accessClaims.getExpiresAt(),
                refreshClaims.getExpiresAt()
        );
    }

    private AppUser getUserByToken(String token) {
        Jwt jwt = this.decoder.decode(token);
        String email = Objects.requireNonNull(jwt.getSubject());
        Instant expiresAt = Objects.requireNonNull(jwt.getExpiresAt());    
        
        if((expiresAt.isBefore(Instant.now()))) {
            throw new InvalidTokenException("Token is not valid");
        }

        return userRepo.findByEmail(email).orElseThrow(() -> {
            LOG.error("User not found: '{}'", email);
            return new UsernameNotFoundException(String.format("User not found: '%s'", email));
        });
    }

    private TokenResponse buildTokenResponse(String access, String refresh, Instant minutes, Instant days) {
        Map<String, Object> accessToken = new HashMap<>(2);
        accessToken.put("token", access);
        accessToken.put("exp", minutes);

        Map<String, Object> refreshToken = new HashMap<>(2);
        refreshToken.put("token", refresh);
        refreshToken.put("exp", days);
        return new TokenResponse("bearer", accessToken, refreshToken);
    }
}
