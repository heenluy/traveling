package dev.henriqueluiz.travelling.api;

import java.util.Map;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.henriqueluiz.travelling.model.util.LoginRequest;
import dev.henriqueluiz.travelling.model.util.RefreshToken;
import dev.henriqueluiz.travelling.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthApi {
    private final static Logger LOG = LoggerFactory.getLogger(AuthApi.class);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/token")
    public Map<String, String> accessToken(@RequestBody @Valid LoginRequest req) throws AuthenticationException {
        LOG.debug("Token requested for user: '{}'", req.email());
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        return jwtService.getAccessToken(authentication);
    }

    @GetMapping("/refresh")
    public Map<String, String> refreshToken(@RequestBody @Valid RefreshToken token) {
        LOG.debug("Access token was been requested");     
        return jwtService.refreshToken(token.refreshToken());
    }

}
