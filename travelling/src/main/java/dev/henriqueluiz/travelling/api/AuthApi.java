package dev.henriqueluiz.travelling.api;

import dev.henriqueluiz.travelling.model.mapper.LoginRequest;
import dev.henriqueluiz.travelling.model.mapper.RefreshToken;
import dev.henriqueluiz.travelling.model.mapper.TokenResponse;
import dev.henriqueluiz.travelling.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class AuthApi {
    private final static Logger LOG = LoggerFactory.getLogger(AuthApi.class);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> accessToken(@RequestBody @Valid LoginRequest req) {
        LOG.debug("Token requested for user: '{}'", req.email());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        TokenResponse response = jwtService.getAccessToken(authentication);
        response.add(linkTo(methodOn(TravelApi.class).saveTravel(null, null)).withRel("createTravel"));
        response.add(linkTo(methodOn(TravelApi.class).getAll(null, null)).withRel("getTravels"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody @Valid RefreshToken token) {
        LOG.debug("Access token was been requested");
        TokenResponse response = jwtService.refreshToken(token.refresh_token());
        response.add(linkTo(methodOn(TravelApi.class).saveTravel(null, null)).withRel("createTravel"));
        response.add(linkTo(methodOn(TravelApi.class).getAll(null, null)).withRel("getTravels"));
        return ResponseEntity.ok(response);
    }

}
