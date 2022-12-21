package dev.henriqueluiz.travelling.model;

import jakarta.validation.constraints.NotNull;

public record RefreshToken(@NotNull String refreshToken) {}
