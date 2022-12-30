package dev.henriqueluiz.travelling.model.mapper;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TravelDto(
        @NotBlank String destination,
        @NotNull @Future @JsonFormat(pattern = "dd-MM-yyyy") LocalDate departureDate,
        @NotNull @Future @JsonFormat(pattern = "dd-MM-yyyy") LocalDate returnDate,
        @NotNull @Min(50) BigDecimal budget
) {}
