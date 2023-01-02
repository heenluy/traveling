package dev.henriqueluiz.travelling.api;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.travelling.model.mapper.AbstractModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class EntryPointApi {

    @RequestMapping(
            value = "/",
            method = GET,
            produces = { "application/hal+json" }
    )
    public ResponseEntity<AbstractModel> rootEntryPoint() {
        var model = new AbstractModel();
        model.add(linkTo(methodOn(UserApi.class).saveUser(null)).withRel("createUser"));
        model.add(linkTo(methodOn(UserApi.class).getRoles()).withRel("getRoles"));
        model.add(linkTo(methodOn(UserApi.class).addRole(null, null)).withRel("addRoleToUser"));
        return ResponseEntity.ok(model);
    }
}
