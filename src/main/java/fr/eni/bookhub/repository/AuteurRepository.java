package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Long> {
    Optional<Auteur> findByNomAndPrenom(String nom, String prenom);
}
