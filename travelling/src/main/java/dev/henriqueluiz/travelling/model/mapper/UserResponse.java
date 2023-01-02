package dev.henriqueluiz.travelling.model.mapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse extends RepresentationModel<UserResponse> {
    String firstName;
    String lastName;
    String email;
    List<String> authorities;
}
