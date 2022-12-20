package dev.henriqueluiz.travelling.service;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.henriqueluiz.travelling.model.AppUser;
import dev.henriqueluiz.travelling.model.RefreshToken;
import dev.henriqueluiz.travelling.repository.TokenRepo;
import dev.henriqueluiz.travelling.repository.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final static Logger LOG = LoggerFactory.getLogger(TokenService.class);
    private final TokenRepo tokenRepo;
    private final UserRepo userRepo;

    public RefreshToken saveToken(String tokenValue, String email) {
        LOG.debug("Saving new refresh token");
        RefreshToken token = new RefreshToken();
        
        AppUser user = userRepo.findByEmail(email)
            .orElseThrow(() -> {
                LOG.debug("User not found: '{}'", email);
                return new UsernameNotFoundException(String.format("User not found: '%s'", email));
            });

        token.setToken(tokenValue);
        token.setUser(user);
        return tokenRepo.save(token);
    }

    public RefreshToken getRefreshToken(String tokenValue) {
        LOG.debug("Fecthing refresh token entity");
        return tokenRepo.findByToken(tokenValue)
            .orElseThrow(() -> {
                LOG.debug("Token not found: '{}'", tokenValue);
                return new EntityNotFoundException(String.format("Token not found: '%s'", tokenValue));
            });
    }

    public Boolean isValidToken(RefreshToken token) {
        LOG.debug("Checking if the token is valid");
        Instant now = Instant.now();
        
        if (!token.getEnabled() || token.getValidUtil().isBefore(now)) {
            LOG.debug("Token is not valid");
            throw new RuntimeException("Token is not valid");
        }
        
        return true;
    }
}
