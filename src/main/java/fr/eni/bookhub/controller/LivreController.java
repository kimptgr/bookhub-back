package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.service.LivreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class LivreController {

    private final LivreService livreService;

    @PostMapping
    public ResponseEntity<Void> ajoutLivre(@RequestBody LivreDTO livreDTO) {
        livreService.ajoutLivre(livreDTO);

        return ResponseEntity.ok().build();
    }

}
