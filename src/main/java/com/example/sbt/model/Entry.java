package com.example.sbt.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.UUID;

public record Entry(
        @UUID(version = {1, 2, 3, 4}, message = "UUID must valid v4 or lower") String uuid,
        @Pattern(regexp = "^\\dX\\dD\\d{2}$", message = "Must follow regex format [0-9]X[0-9]D[0-9][0-9]. e.g - 1X1D14") String id,
        @NotBlank(message = "name must not be blank") String name,
        @NotBlank(message = "likes must not be blank") String likes,
        @NotBlank(message = "transport must not be blank") String transport,
        @PositiveOrZero(message = "avgSpeed must be >= 0") Double avgSpeed,
        @PositiveOrZero(message = "topSpeed must be >= 0") Double topSpeed
) {
}
