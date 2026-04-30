package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "statuts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Statut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Code libelle;

    public enum Code {
        SUR_LISTE_D_ATTENTE,
        EN_ATTENTE_DE_RETRAIT,
        CLOTUREE,
        ANNULEE
    }
}