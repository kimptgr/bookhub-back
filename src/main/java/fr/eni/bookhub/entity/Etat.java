package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etats")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Etat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private Code libelle;

    public enum Code {
        DISPONIBLE,
        RESERVE,
        EMPRUNTE,
        INUTILISABLE
    }

}

