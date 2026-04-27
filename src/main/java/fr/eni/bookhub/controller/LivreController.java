package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.service.LivreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    /**
     * Recherche un livre, deux cas possibles :<br>
     * - Recherche par ISBN (il doit être exact), dans ce cas les filtres de genre et d'état (disponibilité) sont ignorés<br>
     * - Recherche par titre (partiel) OU par nom d'auteur⋅ice (partiel), dans ce cas les autres filtres sont appliqués<br>
     *
     * @return une liste de livres paginée qui correspond aux critères de recherche
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Livre>> rechercherLivres(
            @RequestParam(defaultValue = "", required = false) String saisie,
            @RequestParam(value = "genres", defaultValue = "", required = false) String libellesGenres,
            @RequestParam(value = "disponibilite", defaultValue = "", required = false) Etat.Code libelleEtat,
            @RequestParam(value = "page") @PositiveOrZero(message = "Le numéro de page doit être un entier non négatif") Integer numeroPage,
            @RequestParam(value = "size") @Positive(message = "Le nombre d'éléments par page doit être strictement positif") Integer taillePage
    ) {
        RechercheDTO rechercheDTO = new RechercheDTO(saisie, libellesGenres.split(","), libelleEtat);

        return ResponseEntity.ok(this.livreService.rechercheLivres(rechercheDTO, numeroPage, taillePage));
    }

}
