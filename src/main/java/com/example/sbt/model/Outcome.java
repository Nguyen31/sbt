package com.example.sbt.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

// Debatable if validation is reachable
public record Outcome(
        @NotBlank String name,
        @NotBlank String transport,
        @PositiveOrZero Double topSpeed
) {
}
