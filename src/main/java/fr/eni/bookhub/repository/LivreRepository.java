package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Livre;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long>, JpaSpecificationExecutor<Livre> {
    Optional<Livre> findByIsbn(@NotNull String isbn);
    Optional<Livre> findById(Long id);
}
