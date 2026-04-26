package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.ReservationDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
            //@AuthenticationPrincipal Utilisateur user,
            @RequestBody ReservationDTO reservationDTO) throws AccessDeniedException {
        //TODO delete quand @AuthenticationPrincipal is implemented
        Utilisateur utilisateur = null;
        if (utilisateur == null) {
            utilisateur = new Utilisateur();
            utilisateur.setEmail("mtartine@dej.com");
            utilisateur.setId(1L);
            utilisateur.setRole(Utilisateur.Role.ADMINISTRATEUR);
        }

        LocalDateTime dateReservation = LocalDateTime.now();

        reservationService.reserverLivre(utilisateur, reservationDTO, dateReservation);
        return ResponseEntity.status(201).build();
    }

}
