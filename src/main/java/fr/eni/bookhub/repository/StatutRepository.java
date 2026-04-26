package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Statut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutRepository extends JpaRepository<Statut, Long> {

    Optional<Statut> findByLibelle(Statut.Code label);
}
