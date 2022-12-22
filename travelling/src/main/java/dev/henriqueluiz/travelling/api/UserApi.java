package dev.henriqueluiz.travelling.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import dev.henriqueluiz.travelling.model.AppRole;
import dev.henriqueluiz.travelling.model.AppUser;
import dev.henriqueluiz.travelling.model.util.UserResponse;
import dev.henriqueluiz.travelling.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PostMapping("users/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid AppUser entity) {
        AppUser user = userService.saveUser(entity);  
        URI uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .port(8080)
                .path("/users/save")
                .toUriString()
        );

        List<String> authorities = user.getAuthorities().stream().toList();
        UserResponse userResponse = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), authorities);
        return ResponseEntity.created(uri).body(userResponse);
    }

    @PostMapping("roles/save")
    public ResponseEntity<AppRole> saveRole(@RequestBody @Valid AppRole entity) {
        AppRole role = userService.saveRole(entity);
        URI uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .port(8080)
                .path("/roles/save")
                .toUriString()
        );
        return ResponseEntity.created(uri).body(role);
    }
    
    
    @PutMapping("roles/add-to-user")
    public ResponseEntity<Void> addRole(@RequestParam String role, @RequestParam String email) {
        userService.addRolesToUser(role, email);
        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("users/get-by-email")
    public ResponseEntity<UserResponse> getMethodName(@RequestParam String email) {
        AppUser user = userService.getUserByEmail(email);
        List<String> authorities = user.getAuthorities().stream().toList();
        UserResponse userResponse = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), authorities);
        return ResponseEntity.ok(userResponse);
    }   
}
