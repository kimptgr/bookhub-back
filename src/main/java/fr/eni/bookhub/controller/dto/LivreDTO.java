package fr.eni.bookhub.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.eni.bookhub.validator.Isbn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record LivreDTO(
        @Valid
        @NotEmpty
        List<AuteurDTO> auteurs,
        @NotNull
        String titre,
        @NotNull
        @Isbn
        String isbn,
        @NotEmpty
        List<Long> genres,
        @NotNull
        String synopsis,
        @NotNull
        Long idEtat,
        String urlImage,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
        LocalDate dateDeParution
        ) {
}
