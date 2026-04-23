package fr.eni.bookhub.controller.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ConnexionDTO(
        @NotBlank
        @Pattern(
                regexp = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$",
                message = "L'email n'est pas valide"
        )
        String email,

        @NotBlank
        String password) {
}

