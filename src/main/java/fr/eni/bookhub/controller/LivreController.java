package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.service.LivreService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/{id}")
    public ResponseEntity<Livre> chercheLivre(@PathVariable Long id) {
        return ResponseEntity.ok(livreService.chercheLivreParId(id));
    }

    @GetMapping
    public ResponseEntity<Page<Livre>> rechercherLivres(@Nullable @RequestParam String saisie,
                                                       @RequestParam String genres,
                                                       @RequestParam String disponibilite,
                                                       @RequestParam Integer page,
                                                       @RequestParam Integer size) {

        RechercheDTO rechercheDTO = new RechercheDTO(saisie, genres.split(","), disponibilite);

        Page<Livre> livres = this.livreService.rechercheLivres(rechercheDTO, page, size);

        return ResponseEntity.ok(livres);
    }

}
