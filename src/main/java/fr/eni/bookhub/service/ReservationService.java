package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.ReservationDTO;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Reservation;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.UtilisateurADejaReserveCelivreException;
import fr.eni.bookhub.exception.UtilisateurATropDeReservationsException;
import fr.eni.bookhub.factory.ReservationFactory;
import fr.eni.bookhub.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final int MAX_RESERVATION = 3;

    private final LivreService livreService;

    private final ReservationRepository reservationRepository;

    private final ReservationFactory reservationFactory;

    private final UtilisateurService utilisateurService;

    /**
     * Vérifie si la réservation est autorisée, possible, la crée puis l'enregistre
     *
     * @param utilisateurConnecte
     * @param reservationDTO
     * @param dateReservation
     */
    @Transactional
    public void reserverLivre(Utilisateur utilisateurConnecte, ReservationDTO reservationDTO, LocalDateTime dateReservation) throws AccessDeniedException {
        Utilisateur reservateur = utilisateurService.loadUserById(reservationDTO.reservateurId());

        verifieSiReservationAutorisee(utilisateurConnecte, reservateur);
        verifieReservationsEnCours(reservateur);

        Livre livre = livreService.chercheLivreParIdEtUtilisable(reservationDTO.livreId());
        verifieSiReservationDejaExistante(livre, reservateur);
        int rang = calculRang(livre);
        Reservation reservation = reservationFactory.create(livre, reservateur, dateReservation, rang);

        livreService.updateEtat(livre, Etat.Code.RESERVE);

        reservationRepository.save(reservation);
    }

    /**
     * Vérifie si la personne qui fait la demande à les droits,
     * elle a les droits si :
     * - Rôle UTILISATEUR: Elle fait une demande de réservation pour elle même
     * - Rôle ADMINISTRATEUR ou BIBLIOTHECAIRE: Fait une demande pour elle ou une autre personne non supprimée
     *
     * @param utilisateurConnecte
     * @param reservateur
     */
    private void verifieSiReservationAutorisee(Utilisateur utilisateurConnecte, Utilisateur reservateur)
            throws AccessDeniedException {
        boolean isAdmin = utilisateurConnecte.getRole().equals(Utilisateur.Role.ADMINISTRATEUR);
        boolean isLibrarian = utilisateurConnecte.getRole().equals(Utilisateur.Role.BIBLIOTHECAIRE);
        boolean makeIsOwnLoan = utilisateurConnecte.getRole().equals(Utilisateur.Role.UTILISATEUR)
                && utilisateurConnecte.getId().equals(reservateur.getId());
        if (!isAdmin && !isLibrarian && !makeIsOwnLoan) throw new AccessDeniedException(
                "Cet utilisateur n'a pas les droits pour faire cette réservation.");
    }

    /**
     * Vérifie que l'utilisateur n'est pas déjà sur la liste d'attente du livre
     * @param livre
     * @param reservateur
     */
    private void verifieSiReservationDejaExistante(Livre livre, Utilisateur reservateur) {
//        List<Reservation> mesReservations = reservateur.getReservations();
//        if (mesReservations.stream() .anyMatch(
//                reservation -> !reservation.isEstSupprimee()
//                        && reservation.getLivre().getId().equals(reservationDTO.livreId()) )) {
//            throw new UtilisateurADejaReserveCelivreException();
//        }

        if (reservationRepository.existsByLivreAndUtilisateurAndEstSupprimeeIsFalse(livre, reservateur)) {
            throw new UtilisateurADejaReserveCelivreException();
        }
    }

    /**
     * Vérifie que le reservateur n'a pas déjà atteint son quota.
     * @param reservateur
     */
    private void verifieReservationsEnCours(Utilisateur reservateur) {
//        List<Reservation> reservations = reservationRepository.findAllByUtilisateurAndEstSupprimeeIsFalse(reservateur);
//
//        if (reservations.size() >= MAX_RESERVATION) {
//            throw new UtilisateurATropDeReservationsException("L'utilisateur a dépassé le nombre maximum de réservations actives");
//        }
        int reservationsEnCours = reservationRepository.countByUtilisateurAndEstSupprimeeIsFalse(reservateur);

        if (reservationsEnCours >= MAX_RESERVATION) {
            throw new UtilisateurATropDeReservationsException("L'utilisateur a dépassé le nombre maximum de réservations actives");
        }
    }

    /**
     * Retourne le rang auquel se trouve la réservation
     * @param livre
     * @return
     */
    private int calculRang(Livre livre) {
        int reservationsCount = reservationRepository.findMaxRange(livre);

        return reservationsCount + 1;
    }

}
