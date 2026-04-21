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

    private String libelle;
}