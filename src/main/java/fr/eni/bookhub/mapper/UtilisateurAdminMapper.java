package fr.eni.bookhub.mapper;

import fr.eni.bookhub.controller.dto.UtilisateurAdminDTO;
import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtilisateurAdminMapper {

    public UtilisateurAdminDTO toDTO(Utilisateur utilisateur) {
        return new UtilisateurAdminDTO(
                utilisateur.getId(),
                utilisateur.getEmail(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getRole(),
                utilisateur.isEnabled()
        );
    }

    public List<UtilisateurAdminDTO> toDTOList(List<Utilisateur> utilisateurs) {
        return utilisateurs.stream()
                .map(this::toDTO)
                .toList();
    }

}
