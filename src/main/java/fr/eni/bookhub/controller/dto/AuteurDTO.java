package fr.eni.bookhub.controller.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public record AuteurDTO(    @NotBlank
                            @JsonAlias({"nom"})
                            String nomAuteur,
                            @JsonAlias({"prenom"})
                            String prenomAuteur) {

}
