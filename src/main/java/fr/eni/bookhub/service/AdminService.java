package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.UpdateRoleDTO;
import fr.eni.bookhub.controller.dto.UpdateUtilisateurAdminDTO;
import fr.eni.bookhub.controller.dto.UtilisateurAdminDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.mapper.UtilisateurAdminMapper;
import fr.eni.bookhub.repository.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurAdminMapper mapper;

    // Récupère tous les utilisateurs sauf l'admin connecté
    public List<UtilisateurAdminDTO> getTousLesUtilisateurs(String emailAdmin) {
        return utilisateurRepository.findAllByOrderByNomAsc()
                .stream()
                .filter(u -> !u.getEmail().equals(emailAdmin))  // bloque l'auto-modification
                .map(mapper::toDTO)
                .toList();
    }

    // Modification cas par cas (rôle + verrou)
    @Transactional
    public void modifierUtilisateur(Long id, UpdateUtilisateurAdminDTO dto, String emailAdmin) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur " + id + " introuvable"));

        if (utilisateur.getEmail().equals(emailAdmin)) {
            throw new RuntimeException("Vous ne pouvez pas vous modifier vous-même");
        }

        utilisateur.setRole(dto.role());
        utilisateur.setDesactive(dto.desactive());
        utilisateurRepository.save(utilisateur);
    }

    // Modification en masse (rôle uniquement)
    @Transactional
    public void modifierRolesEnMasse(List<UpdateRoleDTO> updates, String emailAdmin) {
        updates.forEach(update -> {
            Utilisateur utilisateur = utilisateurRepository.findById(update.id())
                    .orElseThrow(() -> new ElementNotFoundException("Utilisateur " + update.id() + " introuvable"));

            if (utilisateur.getEmail().equals(emailAdmin)) return; // on ignore l'admin connecté

            utilisateur.setRole(update.role());
            utilisateurRepository.save(utilisateur);
        });
    }
}