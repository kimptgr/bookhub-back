package fr.eni.bookhub.controller.dto;

import fr.eni.bookhub.entity.Utilisateur;


    // Modification cas par cas
    public record UpdateUtilisateurAdminDTO(
            Utilisateur.Role role,
            boolean desactive
    ) {}

