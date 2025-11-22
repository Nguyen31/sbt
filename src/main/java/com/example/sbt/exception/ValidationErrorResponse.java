package com.example.sbt.exception;

import java.util.List;

public record ValidationErrorResponse(
        String message,
        int status,
        List<String> errors
) {
}
