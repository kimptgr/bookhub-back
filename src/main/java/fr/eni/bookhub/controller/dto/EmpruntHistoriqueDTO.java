package fr.eni.bookhub.controller.dto;

import java.time.LocalDate;

public record EmpruntHistoriqueDTO(
        Long id,
        String titreLivre,
        String urlImageLivre,
        LocalDate dateEmprunt,
        LocalDate dateRetourEffectif
) {
}
