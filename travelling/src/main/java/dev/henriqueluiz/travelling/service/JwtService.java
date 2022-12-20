package dev.henriqueluiz.travelling.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import dev.henriqueluiz.travelling.model.AppUser;
import dev.henriqueluiz.travelling.model.RefreshToken;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder encoder;
    private final TokenService tokenService;

    public Map<String, String> generateToken(Authentication authentication) {
        Instant now = Instant.now();
        
        List<String> scope = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList());
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(30, ChronoUnit.SECONDS))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();
        
        Map<String, String> tokens = new HashMap<>();
        String accessToken = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        String refreshToken = UUID.randomUUID().toString();
        
        // Saving the refresh token in DB
        tokenService.saveToken(refreshToken, authentication.getName());

        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    public Map<String, String> useRefreshToken(String tokenValue) {
        RefreshToken token = tokenService.getRefreshToken(tokenValue);
        Map<String, String> tokens = new HashMap<>();

        if (tokenService.isValidToken(token)) {
            Instant now = Instant.now();
            AppUser user = token.getUser();
            
            List<String> scope = user.getAuthorities().stream()
                .map(r -> r.getName())
                .collect(Collectors.toList());

            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.SECONDS)) // MINUTES
                .subject(user.getEmail())
                .claim("scope", scope)
                .build();

            token.setEnabled(false);
            String accessToken = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            String refreshToken = UUID.randomUUID().toString();
            
            tokenService.saveToken(refreshToken, user.getEmail());
            tokens.put("access_token", accessToken);
            tokens.put("refresh_token", refreshToken);
            return tokens;
        }
        
        return null;
    }

    // TODO: Mudar o tempo de duração do token.
    // TODO: Melhorar o método 'useRefreshToken'.
    // TODO: Criptografar o refresh token.
    // TODO: Criar DTOs.
    // TODO: Fazer os testes.
    // TODO: Criar o recurso 'Travel'. 
}
