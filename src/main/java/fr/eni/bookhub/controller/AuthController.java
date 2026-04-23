package fr.eni.bookhub.controller;


import fr.eni.bookhub.config.JwtUtils;
import fr.eni.bookhub.controller.dto.ConnexionDTO;
import fr.eni.bookhub.controller.dto.InscriptionDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UtilisateurRepository utilisateurRepository;

    public AuthController(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/inscription")
    public ResponseEntity<?> inscription(@RequestBody InscriptionDTO request) {
        if (utilisateurRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(request.email());
        utilisateur.setNom(request.nom());
        utilisateur.setPrenom(request.prenom());
        utilisateur.setMotDePasseChiffre(passwordEncoder.encode(request.password()));
        utilisateur.setRole(fr.eni.bookhub.entity.Utilisateur.Role.UTILISATEUR);
        utilisateur.setDesactive(false);

        utilisateurRepository.save(utilisateur);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Compte créé avec succès"));
    }

    @PostMapping("/connexion")
    public ResponseEntity<?> connexion(@RequestBody ConnexionDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Email ou mot de passe incorrect"));
        }
        String token = jwtUtils.generateToken(request.email());
        return ResponseEntity.ok(Map.of("token", token));
    }

}


