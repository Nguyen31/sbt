package com.giftgo.sbt.exception;

public record ErrorResponse(
        String message,
        int status
) {
}
