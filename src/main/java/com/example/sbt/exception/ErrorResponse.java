package com.example.sbt.exception;

public record ErrorResponse(
        String message,
        int status
) {
}
