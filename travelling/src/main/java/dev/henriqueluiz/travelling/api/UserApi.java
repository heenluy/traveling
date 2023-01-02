package dev.henriqueluiz.travelling.api;

import dev.henriqueluiz.travelling.model.AppRole;
import dev.henriqueluiz.travelling.model.AppUser;
import dev.henriqueluiz.travelling.model.mapper.AbstractModel;
import dev.henriqueluiz.travelling.model.mapper.UserRequest;
import dev.henriqueluiz.travelling.model.mapper.UserResponse;
import dev.henriqueluiz.travelling.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PostMapping("/users/save")
    public ResponseEntity<UserResponse> saveUser(@RequestBody @Valid UserRequest body) {
        AppUser user = userService.saveUser(requestToEntity(body));
        URI uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .port(8080)
                .path("/users/save")
                .toUriString()
        );
        List<String> authorities = user.getAuthorities().stream().toList();
        UserResponse response = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), authorities);
        response.add(linkTo(methodOn(UserApi.class).getRoles()).withSelfRel());
        response.add(linkTo(methodOn(UserApi.class).addRole(null, null)).withSelfRel());
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/roles/save")
    public ResponseEntity<AppRole> saveRole(@RequestBody @Valid AppRole entity) {
        AppRole role = userService.saveRole(entity);
        URI uri = URI.create(
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .port(8080)
                .path("/roles/save")
                .toUriString()
        );
        role.add(linkTo(methodOn(UserApi.class).addRole(null, null)).withSelfRel());
        role.add(linkTo(methodOn(UserApi.class).getRoles()).withSelfRel());
        return ResponseEntity.created(uri).body(role);
    }
    
    
    @PutMapping("/roles/add")
    public ResponseEntity<AbstractModel> addRole(@RequestParam String role, @RequestParam String email) {
        userService.addRolesToUser(role, email);
        var model = new AbstractModel();
        model.add(linkTo(methodOn(AuthApi.class).accessToken(null)).withRel("GetAccessToken"));
        return ResponseEntity.ok(model);
    }
    
    @GetMapping("/roles/get/all")
    public ResponseEntity<CollectionModel<AppRole>> getRoles() {
        List<AppRole> roles = userService.getAllRoles();
        return ResponseEntity.ok(CollectionModel.of(roles));
    }

    @GetMapping("/users/get/by")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email) {
        AppUser user = userService.getUserByEmail(email);
        List<String> authorities = user.getAuthorities().stream().toList();
        UserResponse response = new UserResponse(user.getFirstName(), user.getLastName(), user.getEmail(), authorities);
        response.add(linkTo(methodOn(UserApi.class).getRoles()).withSelfRel());
        return ResponseEntity.ok(response);
    }

    private AppUser requestToEntity(UserRequest request) {
        return Stream.of(request)
                .map(r -> new AppUser(
                                null,
                                r.firstName(),
                                r.lastName(),
                                r.email(),
                                r.password(),
                                new ArrayList<>()))
                .findAny()
                .orElseThrow();
    }
}
