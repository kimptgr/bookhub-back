package fr.eni.bookhub.controller.dto;

import java.time.LocalDate;

public record EmpruntEnCoursDTO(
        Long id,
        String titreLivre,
        String urlImageLivre,
        LocalDate dateRetourPrevisionnel,
        boolean enRetard  //
) {
}
