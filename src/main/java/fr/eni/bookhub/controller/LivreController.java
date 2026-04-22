package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.service.LivreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class LivreController {

    private final LivreService livreService;

    //TODO delete qud security
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<Void> ajoutLivre(@Valid @RequestBody LivreDTO livreDTO) {
        livreService.ajoutLivre(livreDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
