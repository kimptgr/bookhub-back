package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Statut;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.repository.StatutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatutService {
    private final StatutRepository statutRepository;


    public Statut calculStatut(Integer rang, LocalDate dateRetraitMax) {
        LocalDate today = LocalDate.now();
        if (dateRetraitMax != null && today.isAfter(dateRetraitMax)) return retrouveStatut(Statut.Code.ANNULEE);

        if (rang == 1)
            return retrouveStatut(Statut.Code.EN_ATTENTE_DE_RETRAIT);

        return retrouveStatut(Statut.Code.SUR_LISTE_D_ATTENTE);
    }

    private Statut retrouveStatut(Statut.Code statut) {
        return statutRepository.findByLibelle(statut).orElseThrow(() -> new ElementNotFoundException("Statut non trouvé:" + statut));
    }
}
