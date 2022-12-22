package dev.henriqueluiz.travelling.model.util;

import jakarta.validation.constraints.NotNull;

public record RefreshToken(@NotNull String refreshToken) {}
