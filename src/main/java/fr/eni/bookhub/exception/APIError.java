package fr.eni.bookhub.exception;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record APIError(@NotNull String code, @NotNull String message, @NotNull Instant timestamp) {
}
