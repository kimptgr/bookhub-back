package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    //optional pour géré si il ya une erreur de saisie ou une adresse inexistante évitons le NPE)
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);

}
