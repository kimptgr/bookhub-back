package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateMotDePasseDTO(
        @NotBlank String ancienMotDePasse,
        @NotBlank
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).{12,}$",
                message = "Mot de passe trop faible")
        String nouveauMotDePasse
) {
}
