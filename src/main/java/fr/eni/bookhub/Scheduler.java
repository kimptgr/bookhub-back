package fr.eni.bookhub;

import fr.eni.bookhub.service.EmpruntService;
import fr.eni.bookhub.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@Log4j2
public class Scheduler {

    private final ReservationService reservationService;
    private final EmpruntService empruntService;

    /**
     * Tous les jours à 6h
     */
    @Scheduled(cron = "${app.reservation.expiration.cron}")
    public void chercheExpirationScheduled() {
        LocalDate hier = LocalDate.now().minusDays(1);
        log.info("(Scheduled) Cherche les réservations expirées du {}", hier.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        reservationService.recupereLesReservationsExpirees(hier);
    }

    /**
     * Tous les jours à 6h
     */
    @Scheduled(cron = "${app.reservation.expiration.cron}")
    public void chercheEmpruntEnRetard() {
        LocalDate ajd = LocalDate.now();
        log.info("(Scheduled) Cherche les emprunts en retard depuis {}", ajd.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        empruntService.recupereLesEmpruntsEnRetard(ajd);
    }

    /**
     * Au lancement
     */
    //@EventListener(ApplicationReadyEvent.class)
    public void chercheExpirationStart() {
        LocalDate ajd = LocalDate.now().minusDays(1);
        log.info("(Au lancement) Cherche les réservations expirées du {}", ajd);

        reservationService.recupereLesReservationsExpirees(ajd);
        empruntService.recupereLesEmpruntsEnRetard(ajd);
    }

}
