package fr.eni.bookhub.specification;

import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class LivreSpecification {

    /**
     * Crée une Spécification, c'est un système de filtrage géré par JPA qui filtre par Genre et/ou État
     * @param rechercheDTO
     * @see <a href="https://medium.com/devxtalks/implementing-pagination-sorting-and-filtering-in-spring-boot-42615dbd74a7">Exemple sur medium dont je me suis inspiré</a>
     */
    public static Specification<Livre> getSpecificationsForGenreAndEtat(RechercheDTO rechercheDTO) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Si on a un critère sur l'état
            if (rechercheDTO.disponibilite() != null && !rechercheDTO.disponibilite().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("etat").get("libelle"), rechercheDTO.disponibilite()));
            }

            // Si on a un critère sur l'état
            if (rechercheDTO.genres() != null && rechercheDTO.genres().length > 0 && !rechercheDTO.genres()[0].isBlank()) {
                Join<Livre, Genre> genreJoin = root.join("genres");
                predicates.add(genreJoin.get("libelle").in((Object[]) rechercheDTO.genres()));

                // Évite les duplicats si le livre correspond à deux états
                query.distinct(true);
            }

            // Si on a saisi un truc (Auteur ou Titre)
            if (rechercheDTO.saisie() != null && !rechercheDTO.saisie().isBlank()) {
                String pattern = "%" + rechercheDTO.saisie().toLowerCase() + "%";

                Predicate titreLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("titre")), pattern);

                Join<Livre, Auteur> auteurJoin = root.join("auteurs");
                Predicate auteurLike = criteriaBuilder.like(criteriaBuilder.lower(auteurJoin.get("nom")), pattern);

                // On combine les deux conditions avec un OR
                predicates.add(criteriaBuilder.or(titreLike, auteurLike));
            }

            // On retourne un Predicat qui combine toutes les restrictions qu'on a défini
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Crée une Spécification, c'est un système de filtrage géré par JPA qui filtre par isbn
     * @param isbn
     */
    public static Specification<Livre> getSpecificationsForIsbn(String isbn) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("isbn"), isbn);
    }

}
