package fr.eni.bookhub.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "livres")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Livre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private String titre;

    @ManyToMany
    @JoinTable(name = "livres_auteurs",
            joinColumns = @JoinColumn(name = "id_livre"),
            inverseJoinColumns = @JoinColumn(name = "id_auteur")
    )
    @Column(nullable = false)
    private List<Auteur> auteurs;

    private LocalDate dateDeParution;

    @ManyToMany
    @JoinTable(name = "livres_genres",
            joinColumns = @JoinColumn(name = "id_genre"),
            inverseJoinColumns = @JoinColumn(name = "id_livre")
    )
    @Column(nullable = false)
    private List<Genre> genres;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String synopsis;

    @ManyToOne
    @JoinColumn(name = "id_etat", nullable = false)
    private Etat etat;

    @OneToMany(mappedBy = "livre")
    private List<Reservation> reservations;

    @OneToMany
    @JoinColumn(name = "id_livre", nullable = false)
    private List<Avis> avis;

    private String urlImage;

//    public LivreDTO toDTO() {
//        List<AuteurDTO> auteurDTOs = new ArrayList<>();
//        for (Auteur auteur : this.auteurs) {
//            auteurDTOs.add(auteur.toDTO());
//        }
//
//        List<Long> idGenres = new ArrayList<>();
//        for (Genre genre : this.genres) {
//            idGenres.add(genre.getId());
//        }
//
//        return new LivreDTO(auteurDTOs,
//                            this.titre,
//                            this.isbn,
//                            idGenres,
//                            this.synopsis,
//                            this.getEtat().getId(),
//                            this.getUrlImage(),
//                            this.dateDeParution
//        );
//    }
}
