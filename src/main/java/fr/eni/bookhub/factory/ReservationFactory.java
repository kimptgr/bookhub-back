package fr.eni.bookhub.factory;

import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Reservation;
import fr.eni.bookhub.entity.Statut;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.service.StatutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ReservationFactory {
    private static final int DUREE_RESERVATION_BLOQUEE = 14;

    private final StatutService statutService;

    public Reservation create(Livre livre, Utilisateur reservateur, LocalDateTime dateReservation, int rang) {
        var reservation = new Reservation();
        reservation.setLivre(livre);
        reservation.setUtilisateur(reservateur);
        reservation.setDateDemandeReservation(dateReservation);

        reservation.setRang(rang);

        LocalDate dateRetraitMax = calculDateRetraitMax(rang, dateReservation);

        if (rang == 1) {
            reservation.setDateDisponibilite(LocalDate.from(dateReservation));
            reservation.setDateRetraitMax(dateRetraitMax);
        }

        Statut statut = statutService.calculStatut(rang, dateRetraitMax);

        reservation.setStatut(statut);

        reservation.setEstSupprimee(false);

        return reservation;
    }

    /**
     * Retourne la date jusque laquelle le livre est réservée.
     * Si le livre n'est pas encore disponible, retourne null
     */
    private LocalDate calculDateRetraitMax(Integer rang, LocalDateTime dateReservation) {
        return rang == 1 ? LocalDate.from(dateReservation.plusDays(DUREE_RESERVATION_BLOQUEE)) : null;
    }
}
