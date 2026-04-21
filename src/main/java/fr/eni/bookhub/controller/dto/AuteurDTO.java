package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record AuteurDTO(    @NotBlank
                            String nomAuteur,
                            String prenomAuteur) {

}
