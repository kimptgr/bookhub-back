package fr.eni.bookhub.controller.dto;

import lombok.Data;

@Data
public class InscriptionDTO {
    private String nom;
    private String prenom;
    private String email;
    private String password;
}
