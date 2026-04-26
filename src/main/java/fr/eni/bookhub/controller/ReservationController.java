package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.ReservationDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> ajoutReservation(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestBody ReservationDTO reservationDTO) throws AccessDeniedException {

        LocalDateTime dateReservation = LocalDateTime.now();

        reservationService.reserverLivre(utilisateur, reservationDTO, dateReservation);
        return ResponseEntity.status(201).build();
    }

}
