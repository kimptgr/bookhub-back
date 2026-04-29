package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.ReservationDTO;
import fr.eni.bookhub.controller.dto.ReservationProfilDTO;
import fr.eni.bookhub.controller.dto.ReservationResponseDTO;
import fr.eni.bookhub.entity.Reservation;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponseDTO>> voirMesReservations(
            @AuthenticationPrincipal Utilisateur utilisateur
    ) {
        List<ReservationResponseDTO> reservations = reservationService.recupererReservationsParUtilisateur(utilisateur);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("")
    public ResponseEntity<List<Reservation>> voirLesReservations(
            @AuthenticationPrincipal Utilisateur utilisateur
    ) {
        //TODO A implémenter
        return null;
    }
    @PostMapping
    public ResponseEntity<Void> ajoutReservation(
            @AuthenticationPrincipal Utilisateur utilisateur,
            @RequestBody ReservationDTO reservationDTO) throws AccessDeniedException {

        LocalDateTime dateReservation = LocalDateTime.now();

        reservationService.reserverLivre(utilisateur, reservationDTO, dateReservation);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/me/profil")
    public ResponseEntity<List<ReservationProfilDTO>> getReservationsProfil(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(reservationService.getReservationsProfilUtilisateur(utilisateur));
    }

}
