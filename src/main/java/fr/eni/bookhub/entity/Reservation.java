package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private Utilisateur utilisateur;

    private Integer rang;

    @Column(nullable = false)
    private LocalDateTime dateDemandeReservation;

    private LocalDate dateDisponibilite;

    private LocalDate dateRetraitMax;

    @ManyToOne
    @JoinColumn(name = "id_statut", nullable = false)
    private Statut statut;

    private boolean estSupprimee;

}
