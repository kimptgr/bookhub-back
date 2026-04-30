package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotNull;

public record EmpruntDTO(@NotNull Long livreId, @NotNull Long emprunteurId) {
}
