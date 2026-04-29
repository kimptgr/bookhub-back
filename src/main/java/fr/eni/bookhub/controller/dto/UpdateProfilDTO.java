package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateProfilDTO(
        @NotBlank String nom,
        String prenom,
        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$",
                message = "Email invalide")
        String email,
        @Pattern(regexp = "^(0|\\+33[ .-]?0?)\\d([ .-]?\\d{2}){4}$",
                message = "Numéro de téléphone invalide")
        String telephone
) {
}
