package dev.henriqueluiz.travelling.model.mapper;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigInteger;
import java.time.LocalDate;

public record TravelDto(
        @NotBlank String destination,
        @NotNull @Future LocalDate departureDate,
        @NotNull @Future LocalDate returnDate,
        @NotNull @Min(50) BigInteger budget
) {}
