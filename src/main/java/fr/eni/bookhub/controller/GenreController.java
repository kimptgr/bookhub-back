package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.GenreAjoutDTO;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.service.GenreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        return ResponseEntity.ok(genreService.trouverTousLesGenres());
    }

    @PostMapping
    public ResponseEntity<Void> ajoutGenre(@Valid  @RequestBody GenreAjoutDTO genre) {
        genreService.ajouterGenre(genre);
        return ResponseEntity.status(201).build();
    }
}
