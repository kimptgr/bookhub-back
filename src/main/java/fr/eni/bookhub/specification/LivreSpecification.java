package fr.eni.bookhub.specification;

import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LivreSpecification {

    /**
     * Crée une Spécification, c'est un système de filtrage dynamique qui peut être interprété par JPA, celui-ci filtre par Genre et/ou État et/ou Titre et/ou Nom d'auteur⋅ice
     *
     * @see <a href="https://medium.com/devxtalks/implementing-pagination-sorting-and-filtering-in-spring-boot-42615dbd74a7">Exemple sur medium dont je me suis inspiré</a>
     */
    public Specification<Livre> getSpecificationsForGenreOuEtatOuTitreOuNomAuteur(RechercheDTO rechercheDTO) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Si on a un critère sur l'état, on l'applique
            if (rechercheDTO.libellesEtats() != null && rechercheDTO.libellesEtats().length > 0) {
                Join<Livre, Etat> etatJoin = root.join("etat");
                predicates.add(etatJoin.get("libelle").in((Object[]) rechercheDTO.libellesEtats()));

            } else {
                // Sinon, on ne remonte pas les livres inutilisables
                predicates.add(criteriaBuilder.notEqual(root.get("etat").get("libelle"), Etat.Code.INUTILISABLE));
            }

            // Si on a un critère sur les genres, on l'applique
            if (rechercheDTO.libellesGenres() != null && rechercheDTO.libellesGenres().length > 0 && !rechercheDTO.libellesGenres()[0].isBlank()) {
                Join<Livre, Genre> genreJoin = root.join("genres");
                predicates.add(genreJoin.get("libelle").in((Object[]) rechercheDTO.libellesGenres()));

                // On évite les duplicats si le livre correspond à deux genres
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

            // On retourne un Predicat qui combine toutes les restrictions qu'on a définies
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Crée une Spécification, c'est un système de filtrage dynamique qui peut être interprété par JPA, celui-ci filtre par isbn
     */
    public Specification<Livre> getSpecificationsForIsbn(String isbn) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("isbn"), isbn);
    }

}
