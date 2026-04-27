package fr.eni.bookhub.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.eni.bookhub.validator.Isbn;
import jakarta.validation.Valid;


import java.time.LocalDate;
import java.util.List;

public record UpdateLivreDTO(
        @Valid
        List<AuteurDTO> auteurs,
        String titre,
        @Isbn
        String isbn,
        List<Long> genres,
        String synopsis,
        Long idEtat,
        String urlImage,
        @JsonFormat(pattern = "dd/MM/yyyy", shape = JsonFormat.Shape.STRING)
        LocalDate dateDeParution
) {
}

