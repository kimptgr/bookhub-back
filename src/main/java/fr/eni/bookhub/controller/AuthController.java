package fr.eni.bookhub.controller;


import fr.eni.bookhub.config.JwtUtils;
import fr.eni.bookhub.controller.request.LoginRequest;
import fr.eni.bookhub.controller.request.RegisterRequest;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<String> inscription(@RequestBody RegisterRequest request) {
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }

        Utilisateur Utilisateur = new Utilisateur();
        Utilisateur.setEmail(request.getEmail());
        Utilisateur.setNom(request.getNom());
        Utilisateur.setMotDePasseChiffre(passwordEncoder.encode(request.getPassword()));
        Utilisateur.setRole(fr.eni.bookhub.entity.Utilisateur.Role.UTILISATEUR);
        Utilisateur.setDesactive(false);

        utilisateurRepository.save(Utilisateur);
        return ResponseEntity.status(201).body("Compte créé avec succès");
    }

    @PostMapping("/connexion")
    public ResponseEntity<String> connexion(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = jwtUtils.generateToken(request.getEmail());
        return ResponseEntity.ok(token);
    }

}


