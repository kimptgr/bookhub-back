package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record UpdateEmpruntDTO(
        @Positive Long idEmprunt,
        @Positive Long idLivre,
        @Positive Long idEmprunteur,
        @NotNull LocalDateTime dateRetour
) {
}
