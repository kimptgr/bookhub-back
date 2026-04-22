package fr.eni.bookhub.controller.dto;


import jakarta.validation.constraints.NotBlank;


public record ConnexionDTO(
        @NotBlank String email,
        @NotBlank String password) {
}

