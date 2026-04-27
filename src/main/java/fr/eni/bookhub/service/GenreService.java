package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.GenreAjoutDTO;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.exception.GenreDejaExistantException;
import fr.eni.bookhub.repository.GenreRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;

    public List<Genre> retrouverGenres(@NotEmpty List<Long> genres) {
        return genreRepository.findByIdIn(genres);
    }

    public List<Genre> trouverTousLesGenres() {
        return genreRepository.findAllfindAllByOrderByLibelleAsc();
    }

    public void ajouterGenre(GenreAjoutDTO genre) {
        Optional<Genre> genreDejaPresent = genreRepository.findByLibelle(formatter(genre.libelle()));
        if (genreDejaPresent.isPresent()) throw new GenreDejaExistantException();

        var genreEntity = new Genre();
        genreEntity.setLibelle(formatter(genre.libelle()));
        genreRepository.save(genreEntity);
    }

    private static String formatter(String genre) {
        return genre.trim().toUpperCase();
    }

}
