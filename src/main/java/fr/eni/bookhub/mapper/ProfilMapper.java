package fr.eni.bookhub.mapper;

import fr.eni.bookhub.controller.dto.ProfilDTO;
import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.stereotype.Component;

@Component
public class ProfilMapper {
    public ProfilDTO toDTO(Utilisateur utilisateur) {
        return new ProfilDTO(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getTelephone()
        );
    }
}
