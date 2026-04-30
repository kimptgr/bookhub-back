package fr.eni.bookhub.controller.dto;

import org.jspecify.annotations.Nullable;

public record ReservationDTO(@Nullable Long id, Long livreId, Long reservateurId) {
}
