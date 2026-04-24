package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.ReservationDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private UtilisateurService utilisateurService;
    private final ReservationRepository reservationRepository;

    @Transactional
    public void reserverLivre(Utilisateur utilisateur, ReservationDTO reservationDTO, LocalDate dateReservation) {
        Utilisateur reservateur = utilisateurService.loadUserById(reservationDTO.reservateurId());
        if (!verifieSiReservable(reservationDTO)) {
            //TODO throw exception
        };
        verifieSiJePeuxReserver(reservationDTO);


    }

    private boolean verifieSiJePeuxReserver(ReservationDTO reservationDTO) {
        return true;
    }

    private boolean verifieSiReservable(ReservationDTO reservationDTO) {
        return true;
    }
}
