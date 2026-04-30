package fr.eni.bookhub.controller.dto;

import fr.eni.bookhub.entity.Utilisateur;

public record UtilisateurAdminDTO(
        Long id,
        String email,
        String nom,
        String prenom,
        Utilisateur.Role role,
        Boolean desactive
) {
}
