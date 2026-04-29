package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.*;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.IdDiscordantsException;
import fr.eni.bookhub.service.EmpruntService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if (!id.equals(updateEmpruntDTO.idEmprunt())) {
            throw new IdDiscordantsException("Les deux id d'emprunt fournis ne correspondent pas");
        }

        return ResponseEntity.ok(empruntService.updateEmprunt(updateEmpruntDTO));
    }

//    le @AuthenticationPrincipal permet de récupérer les informations utilisateurs depuis le token JWT pas besoin de faire une requête
    @GetMapping("/me")
    public ResponseEntity<List<EmpruntEnCoursDTO>> getEmpruntsEnCours(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(empruntService.getEmpruntsEnCours(utilisateur));
    }

    @GetMapping("/me/historique")
    public ResponseEntity<List<EmpruntHistoriqueDTO>> getHistoriqueEmprunts(
            @AuthenticationPrincipal Utilisateur utilisateur) {
        return ResponseEntity.ok(empruntService.getHistoriqueEmprunts(utilisateur));
    }
}
