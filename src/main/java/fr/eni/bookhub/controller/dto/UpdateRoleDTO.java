package fr.eni.bookhub.controller.dto;

import fr.eni.bookhub.entity.Utilisateur;

// Modification en masse
    public record UpdateRoleDTO(
            Long id,
            Utilisateur.Role role
    ) {}
