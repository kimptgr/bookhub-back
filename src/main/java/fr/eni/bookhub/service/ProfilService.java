package fr.eni.bookhub.service;

import fr.eni.bookhub.config.JwtUtils;
import fr.eni.bookhub.controller.dto.ProfilDTO;
import fr.eni.bookhub.controller.dto.UpdateMotDePasseDTO;
import fr.eni.bookhub.controller.dto.UpdateProfilDTO;
import fr.eni.bookhub.controller.dto.VerifMotDePasseDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.exception.EmailDejaUtiliseException;
import fr.eni.bookhub.mapper.ProfilMapper;
import fr.eni.bookhub.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfilService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfilMapper profilMapper;
    private final JwtUtils jwtUtils;


    // Récupère le profil de l'utilisateur connecté
    public ProfilDTO getProfil(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur introuvable"));
        return profilMapper.toDTO(utilisateur);
    }

    // Modifie nom, prénom, email
    @Transactional
    public String updateProfil(String email, UpdateProfilDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur introuvable"));

        // Si l'email change, on vérifie qu'il n'est pas déjà pris
        if (!utilisateur.getEmail().equals(dto.email())) {
            if (utilisateurRepository.existsByEmail(dto.email())) {
                throw new EmailDejaUtiliseException(dto.email());
            }
        }

        utilisateur.setNom(dto.nom());
        utilisateur.setPrenom(dto.prenom());
        utilisateur.setEmail(dto.email());
        utilisateurRepository.save(utilisateur);

        return jwtUtils.generateToken(utilisateur.getEmail(), utilisateur.getId(), utilisateur.getRole().name());
    }
    
    

    // Vérifie que l'ancien mot de passe est correct
    public void verifierAncienMotDePasse(String email, VerifMotDePasseDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(dto.ancienMotDePasse(), utilisateur.getMotDePasseChiffre())) {
            throw new RuntimeException("Mot de passe incorrect");
        }
    }

    // Change le mot de passe
    @Transactional
    public String updateMotDePasse(String email, UpdateMotDePasseDTO dto) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(dto.ancienMotDePasse(), utilisateur.getMotDePasseChiffre())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        utilisateur.setMotDePasseChiffre(passwordEncoder.encode(dto.nouveauMotDePasse()));
        utilisateurRepository.save(utilisateur);

        return jwtUtils.generateToken(utilisateur.getEmail(), utilisateur.getId(), utilisateur.getRole().name());
    }

    // Suppression physique du compte
    @Transactional
    public void supprimerCompte(String email) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur introuvable"));
        utilisateurRepository.delete(utilisateur);
    }
}