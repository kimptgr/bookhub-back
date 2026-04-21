package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotNull;

public record AuteurDTO(    @NotNull
                            String nomAuteur,
                            String prenomAuteur) {

}
