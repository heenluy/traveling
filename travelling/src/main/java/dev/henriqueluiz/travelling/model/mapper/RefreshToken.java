package dev.henriqueluiz.travelling.model.mapper;

import jakarta.validation.constraints.NotNull;

public record RefreshToken(@NotNull String refresh_token) {}
