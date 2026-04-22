package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record InscriptionDTO (
        @NotBlank String nom,
                  String prenom,
        @NotBlank String email,
        @NotBlank String password
        ) {

}
