package fr.eni.bookhub.controller.dto;

import java.time.LocalDate;

public record ReservationProfilDTO(
        Long id,
        String titreLivre,
        String urlImageLivre,
        int rang,
        String statutLibelle,
        LocalDate dateDisponibilite
) {
}
