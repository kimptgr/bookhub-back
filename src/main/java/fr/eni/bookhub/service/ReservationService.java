package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.ReservationDTO;
import fr.eni.bookhub.controller.dto.ReservationProfilDTO;
import fr.eni.bookhub.controller.dto.ReservationResponseDTO;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Reservation;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.exception.UtilisateurADejaReserveCelivreException;
import fr.eni.bookhub.exception.UtilisateurATropDeReservationsException;
import fr.eni.bookhub.exception.emprunt.PasPremierSurListeDAttenteException;
import fr.eni.bookhub.exception.reservation.UtilisateurQuiEmprunteNePeutPasReserverException;
import fr.eni.bookhub.factory.ReservationFactory;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationService {
    private static final int MAX_RESERVATION = 3;

    private final LivreService livreService;

    private final ReservationFactory reservationFactory;

    private final ReservationRepository reservationRepository;

    private final EmpruntRepository empruntRepository;

    private final UtilisateurService utilisateurService;

    /**
     * Vérifie si la réservation est autorisée, possible, la crée puis l'enregistre
     *
     */
    @Transactional
    public void reserverLivre(Utilisateur utilisateurConnecte, ReservationDTO reservationDTO, LocalDateTime dateReservation) throws AccessDeniedException {
        Utilisateur reservateur = utilisateurService.loadUserById(reservationDTO.reservateurId());

        verifieSiReservationAutorisee(utilisateurConnecte, reservateur);
        verifieReservationsEnCours(reservateur);

        Livre livre = livreService.chercheLivreParIdEtUtilisable(reservationDTO.livreId());
        verifieEmpruntEnCours(reservateur, livre);
        verifieSiReservationDejaExistante(livre, reservateur);
        int rang = calculRang(livre);
        Reservation reservation = reservationFactory.create(livre, reservateur, dateReservation, rang);

        if (livre.getEtat().getLibelle().equals(Etat.Code.DISPONIBLE))
            livreService.updateEtat(livre, Etat.Code.RESERVE);

        reservationRepository.save(reservation);
    }


    /**
     * Vérifie si la personne qui fait la demande à les droits,
     * elle a les droits si :
     * - Rôle UTILISATEUR: Elle fait une demande de réservation pour elle même
     * - Rôle ADMINISTRATEUR ou BIBLIOTHECAIRE: Fait une demande pour elle ou une autre personne non supprimée
     *
     */
    private void verifieSiReservationAutorisee(Utilisateur utilisateurConnecte, Utilisateur reservateur)
            throws AccessDeniedException {
        boolean isAdmin = utilisateurConnecte.getRole().equals(Utilisateur.Role.ADMINISTRATEUR);
        boolean isLibrarian = utilisateurConnecte.getRole().equals(Utilisateur.Role.BIBLIOTHECAIRE);
        boolean makeIsOwnLoan = utilisateurConnecte.getRole().equals(Utilisateur.Role.UTILISATEUR)
                && utilisateurConnecte.getId().equals(reservateur.getId());
        if (!isAdmin && !isLibrarian && !makeIsOwnLoan) throw new AccessDeniedException(
                "Cet utilisateur n'a pas les droits pour cette réservation.");
    }

    /**
     * Vérifie que l'utilisateur n'est pas déjà sur la liste d'attente du livre ou qu'il n'a pas emprunté le livre
     *
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
     *
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
     *
     */
    private int calculRang(Livre livre) {
        int reservationsCount = reservationRepository.findMaxRange(livre);

        return reservationsCount + 1;
    }

    /**
     * Récupère les réservationsDTO par utilisateur qui ne sont pas supprimées
     *
     */
    public List<ReservationResponseDTO> recupererReservationsParUtilisateur(Utilisateur utilisateur) {
        return reservationRepository.findByUtilisateurAndEstSupprimee(utilisateur, false).stream().map(
                reservation -> new ReservationResponseDTO(
                        reservation.getId(),
                        reservation.getLivre().getId(),
                        reservation.getUtilisateur().getId(),
                        reservation.getRang(),
                        reservation.getDateDemandeReservation(),
                        reservation.getDateDisponibilite(),
                        reservation.getDateRetraitMax(),
                        reservation.getStatut().getLibelle().toString(),
                        reservation.isEstSupprimee()
                )
        ).toList();
    }

    /**
     * Vérifie que l'utilisateur est le premier sur la liste d'attente du livre et met à jour les réservations
     * (état supprimé pour la réservation de l'emprunteur et rang -1 aux autres)
     *
     */
    public void updateReservationLieesAuLivre(Livre livre, Utilisateur emprunteur) {
        List<Reservation> reservationList = reservationRepository.findAllByLivreAndEstSupprimeeIsFalse(livre);

        reservationList.forEach((reservation) -> {
            if (reservation.getUtilisateur().getId().equals(
                    emprunteur.getId()) && reservation.getRang() != 1) throw new PasPremierSurListeDAttenteException();

            if (reservation.getUtilisateur().getId().equals(
                    emprunteur.getId())) reservation.setEstSupprimee(true);

            reservation.setRang(reservation.getRang() - 1);
        });
        reservationRepository.saveAll(reservationList);
    }

    /**
     * Vérifie que le réservateur n'est pas emprunteur du livre
     *
     */
    private void verifieEmpruntEnCours(Utilisateur reservateur, Livre livre) {
        boolean empruntEncours = empruntRepository.existsByLivreAndUtilisateurAndDateRetourEffectifIsNull(livre, reservateur);

        if (empruntEncours) {
            throw new UtilisateurQuiEmprunteNePeutPasReserverException();
        }
    }

    @Transactional
    public void supprimeReservation(Long reservationId) {
        Reservation toDelete = reservationRepository.findById(reservationId).orElseThrow(() -> new ElementNotFoundException("Reservation non trouvée"));
        if (toDelete.isEstSupprimee()) { return;
        }

        int deletedRank = toDelete.getRang();
        Long livreId = toDelete.getLivre().getId();
        toDelete.setEstSupprimee(true);

        List<Reservation> reservations = reservationRepository .findAllByLivreIdAndEstSupprimeeIsFalse(livreId);
        if (reservations.isEmpty()) {
            livreService.updateEtat(toDelete.getLivre(), Etat.Code.DISPONIBLE);
        }

        reservations.forEach(reservation -> {
            if (reservation.getRang() > deletedRank) { reservation.setRang(reservation.getRang() - 1);
            }
            if (deletedRank == 1 && reservation.getRang() == 1) {
                reservation.setDateDisponibilite(LocalDate.now());
                reservation.setDateRetraitMax(LocalDate.now().plusDays(14));
            }
        });
        reservationRepository.saveAll(reservations);
    }

/**
 * Récupère les réservations expirées
 * - Update l'état estSupprimée à vrai
 * - Met à jour la queue
 * - Logs quand la réservation change d'emprunteur
 */
    @Transactional
    public void recupereLesReservationsExpirees(LocalDate date) {
        List<Reservation> expiredReservations =
                reservationRepository.findAllByDateRetraitMaxAndEstSupprimeeIsFalse(date);

        for (Reservation expired : expiredReservations) {

            Livre livre = expired.getLivre();

            Optional<Reservation> expiree = reservationRepository
                    .findFirstByLivreAndEstSupprimeeIsFalseOrderByRangAsc(livre);

            Long oldReservateurId = expiree.map(r -> r.getUtilisateur().getId()).orElse(null);


            supprimeReservation(expiree.get().getId());

            Optional<Reservation> nouveauReserveur = reservationRepository
                    .findFirstByLivreAndEstSupprimeeIsFalseOrderByRangAsc(livre);

            Long nouveauReserveurId = nouveauReserveur.map(r -> r.getUtilisateur().getId()).orElse(null);

            if (!Objects.equals(oldReservateurId, nouveauReserveurId)) {
                log.warn("Main reserver changed for book {}: {} -> {}",
                        livre.getId(),
                        oldReservateurId,
                        nouveauReserveurId);
            }
        }
    }

    public void verifieSiUtilisateurPeutSupprimer(Utilisateur userConnecte, Long idReservation) throws AccessDeniedException {
        Long userId = userConnecte.getId();
        Utilisateur.Role role = userConnecte.getRole();
        boolean isAdmin = userConnecte.getRole().equals(Utilisateur.Role.ADMINISTRATEUR);
        boolean isLibrarian = userConnecte.getRole().equals(Utilisateur.Role.BIBLIOTHECAIRE);
        Reservation r = reservationRepository.findById(idReservation).orElseThrow(() -> new ElementNotFoundException("Reservation non trouvée"));
        Long rId = r.getUtilisateur().getId();
        boolean isUser = Objects.equals(Utilisateur.Role.UTILISATEUR, role);
        boolean isReservateur = Objects.equals(rId, userId);
        boolean makeIsOwnLoan = isUser && isReservateur;

        if (!isAdmin && !isLibrarian && !makeIsOwnLoan) throw new AccessDeniedException(
                "Cet utilisateur n'a pas les droits pour cette réservation.");
    }


    public List<ReservationProfilDTO> getReservationsProfilUtilisateur(Utilisateur utilisateur) {
        return reservationRepository
                .findByUtilisateurAndEstSupprimee(utilisateur, false)
                .stream()
                .map(r -> {

                    // Vérifie s'il existe un emprunt en cours pour ce livre
                    boolean empruntEnCours = empruntRepository
                            .existsByLivreIdAndDateRetourEffectifIsNull(r.getLivre().getId());

                    // permet de savoir si c'est ton tour
                    boolean disponible = (r.getRang() == 1) && !empruntEnCours;

                    return new ReservationProfilDTO(
                            r.getId(),
                            r.getLivre().getTitre(),
                            r.getLivre().getUrlImage(),
                            r.getRang(),
                            r.getStatut().getLibelle().toString(),
                            r.getDateDisponibilite(),
                            disponible
                    );
                })
                .toList();
    }

}
