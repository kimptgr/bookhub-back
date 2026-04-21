package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "utilisateurs")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasseChiffre;

    @OneToMany(mappedBy = "utilisateur")
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "utilisateur")
    private List<Avis> avis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) CHECK (role IN ('UTILISATEUR', 'BIBLIOTHECAIRE', 'ADMINISTRATEUR'))")
    private Role role;

    @Column(nullable = false)
    private boolean desactive;

    public enum Role {
        UTILISATEUR,
        BIBLIOTHECAIRE,
        ADMINISTRATEUR
    }

}
