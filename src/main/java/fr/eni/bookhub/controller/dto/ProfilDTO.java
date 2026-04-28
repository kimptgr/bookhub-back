package fr.eni.bookhub.controller.dto;

public record ProfilDTO(
        Long id,
        String nom,
        String prenom,
        String email
) {
}
