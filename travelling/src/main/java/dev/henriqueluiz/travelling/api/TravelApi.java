package dev.henriqueluiz.travelling.api;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.travelling.model.AppUser;
import dev.henriqueluiz.travelling.model.Travel;
import dev.henriqueluiz.travelling.model.mapper.TravelDto;
import dev.henriqueluiz.travelling.service.TravelService;
import dev.henriqueluiz.travelling.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class TravelApi {
    private final TravelService travelService;
    private final UserService userService;

    @PostMapping("/travels/save")
    public ResponseEntity<TravelDto> saveTravel(Principal principal, @RequestBody @Valid TravelDto request) {
        AppUser user = userService.getUserByEmail(principal.getName());
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .port(8080)
                        .path("/travels/save")
                        .toUriString()
        );
        travelService.saveTravel(requestToEntity(request, user));
        return ResponseEntity.created(uri).body(request);
    }

    @PutMapping("/travels/update")
    public ResponseEntity<TravelDto> updateTravel(Principal principal, @RequestParam Long id, @RequestBody @Valid TravelDto request) {
        AppUser user = userService.getUserByEmail(principal.getName());
        travelService.updateTravel(id, requestToEntity(request, user));
        return ResponseEntity.accepted().body(request);
    }

    @DeleteMapping("/travels/delete")
    public ResponseEntity<?> deleteTravel(Principal principal, @RequestParam Long id) {
        travelService.deleteTravel(id, principal.getName());
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/travels/get")
    public ResponseEntity<?> getById(Principal principal, @RequestParam Long id) {
        TravelDto response = entityToResponse(travelService.getById(id, principal.getName()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/travels/get/all")
    public ResponseEntity<Page<TravelDto>> getAll(Principal principal, Pageable pageable) {
        Page<Travel> travels = travelService.getAllByUser(principal.getName(), pageable);
        return ResponseEntity.ok(travels.map(this::entityToResponse));
    }

    private Travel requestToEntity(TravelDto request, AppUser user) {
        return new Travel(null,
                request.destination(),
                request.departureDate(),
                request.returnDate(),
                request.budget(),
                user);
    }

    private TravelDto entityToResponse(Travel entity) {
        return new TravelDto(
                entity.getDestination(),
                entity.getDepartureDate(),
                entity.getReturnDate(),
                entity.getBudget()
        );
    }
}
