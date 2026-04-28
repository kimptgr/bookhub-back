package fr.eni.bookhub.repository.view;

import fr.eni.bookhub.entity.*;

import java.time.LocalDate;
import java.util.List;

public record LivreView(
        Long id,
        String isbn,
        String titre,
        List<Auteur> auteurs,
        LocalDate dateDeParution,
        List<Genre> genres,
        String synopsis,
        Etat etat,
        Long idEmpruntActif,
        Long idEmprunteur,
        List<Avis> avis,
        String urlImage
) {
}