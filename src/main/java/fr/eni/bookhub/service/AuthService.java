package fr.eni.bookhub.service;

import fr.eni.bookhub.config.JwtUtils;
import fr.eni.bookhub.controller.dto.ConnexionDTO;
import fr.eni.bookhub.controller.dto.InscriptionDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.EmailDejaUtiliseException;
import fr.eni.bookhub.repository.UtilisateurRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public void inscrire(InscriptionDTO request) {
        verifierEmailDisponible(request.email());

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(request.email());
        utilisateur.setNom(request.nom());
        utilisateur.setPrenom(request.prenom());
        utilisateur.setMotDePasseChiffre(passwordEncoder.encode(request.password()));
        utilisateur.setRole(Utilisateur.Role.UTILISATEUR);
        utilisateur.setDesactive(false);

        utilisateurRepository.save(utilisateur);
    }

    public String connecter(ConnexionDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        return jwtUtils.generateToken(request.email());
    }

    public void verifierEmailDisponible(String email) {
        if (utilisateurRepository.existsByEmail(email)) {
            throw new EmailDejaUtiliseException(email);
        }
    }


}