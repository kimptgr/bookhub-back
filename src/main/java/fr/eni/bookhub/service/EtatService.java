package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.repository.EtatRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EtatService {
    private final EtatRepository etatRepository;

    public Etat retrouveEtat(@NotNull Long id) {
        return etatRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Etat non trouvé: " + id));
    }

    public Etat retrouveEtatParLibelle(Etat.Code label) {
        return etatRepository.findByLibelle(label).orElseThrow(() -> new ElementNotFoundException("Etat non trouvé: " + label));
    }

    public @Nullable List<Etat> trouverTousLesEtats() {
        return etatRepository.findAll();
    }
}
