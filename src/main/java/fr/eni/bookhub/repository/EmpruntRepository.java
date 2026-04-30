package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    int countByUtilisateurAndDateRetourEffectifIsNull(Utilisateur emprunteur);

    int countByUtilisateurAndDateRetourEffectifIsNullAndDateRetourPrevisionnelIsBefore(Utilisateur emprunteur, LocalDate now);

    boolean existsByLivreAndDateRetourEffectifIsNull(Livre livre);

    boolean existsByLivreAndUtilisateurAndDateRetourEffectifIsNull(Livre livre, Utilisateur utilisateur);

    List<Emprunt> findByUtilisateurAndDateRetourEffectifIsNull(Utilisateur utilisateur);

    List<Emprunt> findByUtilisateurAndDateRetourEffectifIsNotNull(Utilisateur utilisateur);

    boolean existsByLivreIdAndDateRetourEffectifIsNull(Long livreId);

    List<Emprunt> findAllByDateRetourPrevisionnelBeforeAndDateRetourEffectifIsNull(LocalDate date);
}
