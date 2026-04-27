package fr.eni.bookhub.controller.dto;

import java.time.LocalDate;

public record EmpruntMisAJourDTO(
        Long empruntId,
        Long livreId,
        Long emprunteurId,
        LocalDate dateEmprunt,
        LocalDate dateRetourPrevisionnel,
        LocalDate dateRetourEffectif) {
}
