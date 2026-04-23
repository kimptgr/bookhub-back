package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.ConnexionDTO;
import fr.eni.bookhub.controller.dto.InscriptionDTO;
import fr.eni.bookhub.exception.EmailDejaUtiliseException;
import fr.eni.bookhub.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody InscriptionDTO request) {
        try {
            authService.inscrire(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Compte créé avec succès"));
        } catch (EmailDejaUtiliseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody ConnexionDTO request) {
        try {
            String token = authService.connecter(request);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}