package dev.henriqueluiz.travelling.model.mapper;

import java.util.List;

public record UserResponse(
    String firstName,
    String lastName,
    String email,
    List<String> authorities
) {}
