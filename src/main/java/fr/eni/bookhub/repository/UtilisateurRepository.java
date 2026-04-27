package fr.eni.bookhub.repository;

import fr.eni.bookhub.controller.dto.UtilisateurResponseDTO;
import fr.eni.bookhub.entity.Utilisateur;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    //optional pour géré si il ya une erreur de saisie ou une adresse inexistante évitons le NPE)
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Utilisateur> findByIdAndDesactiveIsFalse(Long id);

    @Query(
        """
           SELECT u.id, u.email FROM Utilisateur u WHERE u.desactive = false
        """
    )
   List<UtilisateurResponseDTO> findAllAndAndDesactiveIsFalse();
}
