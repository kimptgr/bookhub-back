package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.exception.GenresNonCorrespondantException;
import fr.eni.bookhub.exception.LivreDejaExistantException;
import fr.eni.bookhub.mapper.LivreMapper;
import fr.eni.bookhub.repository.LivreRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static fr.eni.bookhub.utils.TextFormatter.isbnFormatteur;

@Validated
@Service
@RequiredArgsConstructor
public class LivreService {
    private final LivreMapper livreMapper;

    private final AuteurService auteurService;
    private final GenreService genreService;
    private final EtatService etatService;

    private final LivreRepository livreRepository;

    @Transactional
    public void ajoutLivre(@NotNull LivreDTO livreDTO) {

        if (livreRepository.findByIsbn(isbnFormatteur(livreDTO.isbn())).isPresent()) {
            throw new LivreDejaExistantException(livreDTO.isbn());
        }

        Livre livre = livreMapper.toEntity(livreDTO);
        List<Auteur> auteurs = auteurService.retrouverAuteurs(livreDTO.auteurs());
        livre.setAuteurs(auteurs);
        List<Genre> genres = genreService.retrouverGenres(livreDTO.genres());
        if (genres.size() != livreDTO.genres().size())
            throw new GenresNonCorrespondantException("");

        livre.setGenres(genres);
        livre.setEtat(etatService.retrouveEtat(livreDTO.idEtat()));

        livreRepository.save(livre);
    }
}
