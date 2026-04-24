package fr.eni.bookhub.entity;

import fr.eni.bookhub.controller.dto.AuteurDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "auteurs")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Auteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String prenom;

    public Auteur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public AuteurDTO toDTO() {
        return new AuteurDTO(this.nom, this.prenom);
    }
}
