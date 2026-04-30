package fr.eni.bookhub.controller.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationResponseDTO(
        Long id,
        Long livreId,
        Long reservateurId,
        int rang,
        LocalDateTime dateDemandeReservation,
        LocalDate dateDisponibilite,
        LocalDate dateRetraitMax,
        String statutLibelle,
        boolean estSupprimee
) {

}
