package fr.eni.bookhub.controller;

import fr.eni.bookhub.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    @PostMapping
    public ResponseEntity<Void> ajoutReservation() {
        reservationService.reserverLivre();
        return ResponseEntity.status(201).build();
    }
}
