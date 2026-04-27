package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.UtilisateurResponseDTO;
import fr.eni.bookhub.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/utilisateurs")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;
    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDTO>> getAllUser() {
        return ResponseEntity.ok(utilisateurService.recupererTousLesUtilisateursActifs());
    }
}
