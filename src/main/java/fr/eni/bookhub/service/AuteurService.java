package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.AuteurDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.repository.AuteurRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static fr.eni.bookhub.utils.TexteFormateur.convertToTitleCase;

@Service
@RequiredArgsConstructor
public class AuteurService {
    private final AuteurRepository auteurRepository;
    /**
     * Retourne une liste d'auteurs persistés d'après une liste de DTOs
     * @param auteurs
     * @return
     */
    public List<Auteur> retrouverAuteurs(@NotEmpty List<AuteurDTO> auteurs) {
        List<Auteur> auteursRetournes = new ArrayList<>();
        auteurs.forEach( auteur ->
                auteursRetournes.add(recupereAuteur(auteur))
        );
        return auteursRetournes;
    }

    /**
     * Retourne l'entité associée au DTO s'il existe en base de données, le crée au besoin
     * @param auteur
     * @return
     */
    public Auteur recupereAuteur(AuteurDTO auteur) {
        Optional<Auteur> auteurOptional = auteurRepository.findByNomAndPrenom(auteur.nomAuteur(), auteur.prenomAuteur());
        return auteurOptional.orElseGet(() -> ajouterAuteur(auteur));
    }

    public Auteur ajouterAuteur(AuteurDTO auteur) {
        return auteurRepository.save(new Auteur(auteur.nomAuteur().toUpperCase(), convertToTitleCase(auteur.prenomAuteur())));
    }
}
