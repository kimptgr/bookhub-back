package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.repository.GenreRepository;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    public List<Genre> retrouverGenres(@NotEmpty List<Long> genres) {
        return genreRepository.findByIdIn(genres);
    }
}
