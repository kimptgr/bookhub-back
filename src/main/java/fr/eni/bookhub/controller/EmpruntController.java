package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.EmpruntDTO;
import fr.eni.bookhub.controller.dto.EmpruntMisAJourDTO;
import fr.eni.bookhub.controller.dto.UpdateEmpruntDTO;
import fr.eni.bookhub.exception.IdDiscordantsException;
import fr.eni.bookhub.service.EmpruntService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emprunts")
@RequiredArgsConstructor
public class EmpruntController {
    private final EmpruntService empruntService;

    @PostMapping
    public ResponseEntity<Void> emprunterLivre(@Valid @RequestBody EmpruntDTO empruntDTO) {
        empruntService.ajoutEmprunt(empruntDTO);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EmpruntMisAJourDTO> updateEmprunt(
            @Positive @PathVariable Long id,
            @Valid @RequestBody UpdateEmpruntDTO updateEmpruntDTO) {
        if (!id.equals(updateEmpruntDTO.idResa())) {
            throw new IdDiscordantsException("Les deux id d'emprunt fournis ne correspondent pas");
        }

        return ResponseEntity.ok(empruntService.updateEmprunt(updateEmpruntDTO));
    }
}
