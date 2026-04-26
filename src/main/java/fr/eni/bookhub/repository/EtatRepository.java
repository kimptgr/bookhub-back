package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Etat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EtatRepository extends JpaRepository<Etat, Long> {
    Optional<Etat> findById(Long id);
    List<Etat> findAll();
    Optional<Etat> findByLibelle(Etat.Code label);
}
