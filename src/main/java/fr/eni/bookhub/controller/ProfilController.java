package fr.eni.bookhub.controller;


import fr.eni.bookhub.controller.dto.ProfilDTO;
import fr.eni.bookhub.controller.dto.UpdateMotDePasseDTO;
import fr.eni.bookhub.controller.dto.UpdateProfilDTO;
import fr.eni.bookhub.controller.dto.VerifMotDePasseDTO;
import fr.eni.bookhub.service.ProfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/profil/me")
@RequiredArgsConstructor
public class ProfilController {

    private final ProfilService profilService;

    private String getEmail(Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("")
    public ResponseEntity<ProfilDTO> getProfil(Authentication authentication) {
        return ResponseEntity.ok(profilService.getProfil(getEmail(authentication)));
    }

    @PatchMapping("")
    public ResponseEntity<Map<String, String>> updateProfil(
            @Valid @RequestBody UpdateProfilDTO dto,
            Authentication authentication) {
        String nouveauToken = profilService.updateProfil(getEmail(authentication), dto);
        // retourne le nouveau token si l'email est changé
        return ResponseEntity.ok(Map.of("token", nouveauToken));
    }

    @PostMapping("/verifier-mot-de-passe")
    public ResponseEntity<Void> verifierMotDePasse(
            @Valid @RequestBody VerifMotDePasseDTO dto,
            Authentication authentication) {
        profilService.verifierAncienMotDePasse(getEmail(authentication), dto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/mot-de-passe")
    public ResponseEntity<Map<String, String>> updateMotDePasse(
            @Valid @RequestBody UpdateMotDePasseDTO dto,
            Authentication authentication) {
        String nouveauToken = profilService.updateMotDePasse(getEmail(authentication), dto);
        //retourne le nouveau token si le mot de passe est changé
        return ResponseEntity.ok(Map.of("token", nouveauToken));
    }

    @DeleteMapping("")
    public ResponseEntity<Void> supprimerCompte(Authentication authentication) {
        profilService.supprimerCompte(getEmail(authentication));
        return ResponseEntity.noContent().build();
    }
}