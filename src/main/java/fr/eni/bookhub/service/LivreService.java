package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.repository.LivreRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class LivreService {
    private final AuteurService auteurService;
    private final GenreService genreService;
    private final EtatService etatService;

    private final LivreRepository livreRepository;

    @Transactional
    public void ajoutLivre(@NotNull LivreDTO livreDTO) {
        Livre livre = mapDTOToLivre(livreDTO);
        List<Auteur> auteurs = auteurService.retrouverAuteurs(livreDTO.auteurs());
        livre.setAuteurs(auteurs);
        List<Genre> genres = genreService.retrouverGenres(livreDTO.genres());
        livre.setGenres(genres);
        livre.setEtat(etatService.retrouveEtat(livreDTO.idEtat()));
        livreRepository.save(livre);
    }

    private Livre mapDTOToLivre(@NotNull LivreDTO livreDTO) {
        Livre livre = new Livre();
        livre.setIsbn(livreDTO.isbn());
        livre.setTitre(livreDTO.titre());
        livre.setDateDeParution(livreDTO.dateDeParution());
        livre.setSynopsis(livreDTO.synopsis());
        livre.setUrlImage(livreDTO.urlImage());
        return livre;
    }
}
