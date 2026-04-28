package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifMotDePasseDTO(
        @NotBlank String ancienMotDePasse
) {
}
