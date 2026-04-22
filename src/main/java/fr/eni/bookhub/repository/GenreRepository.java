package fr.eni.bookhub.repository;

import fr.eni.bookhub.entity.Genre;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findByIdIn(@NotEmpty List<Long> genres);
    List<Genre> findAll();
}
