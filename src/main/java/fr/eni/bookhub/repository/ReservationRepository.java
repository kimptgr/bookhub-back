package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Reservation;
import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByUtilisateurAndEstSupprimeeIsFalse(Utilisateur user);

    boolean existsByLivreAndUtilisateurAndEstSupprimeeIsFalse(Livre livre, Utilisateur user);

    @Query("""
            SELECT coalesce(MAX(rang), 0) from Reservation WHERE livre = :livre and estSupprimee is false
            """)
    int findMaxRange(Livre livre);

    int countByUtilisateurAndEstSupprimeeIsFalse(Utilisateur utilisateur);
}