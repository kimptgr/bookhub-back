package fr.eni.bookhub;

import fr.eni.bookhub.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Log4j2
public class Scheduler {

    private final ReservationService reservationService;

    /**
     * Tous les jours à 6h
     */
    @Scheduled(cron = "${app.reservation.expiration.cron}")
    public void chercheExpirationScheduled() {
        LocalDate hier = LocalDate.now().minusDays(1);
        log.info("(Scheduled) Cherche les réservations expirées du {}", hier);

        reservationService.recupereLesReservationsExpirees(hier);
    }

    /**
     * Au lancement
     */
    @EventListener(ApplicationReadyEvent.class)
    public void chercheExpirationStart() {
        LocalDate hier = LocalDate.now().minusDays(1);
        log.info("(Au lancement) Cherche les réservations expirées du {}", hier);

        reservationService.recupereLesReservationsExpirees(hier);
    }
}
