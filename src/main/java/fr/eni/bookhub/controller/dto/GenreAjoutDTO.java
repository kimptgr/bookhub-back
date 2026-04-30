package fr.eni.bookhub.controller.dto;

import jakarta.validation.constraints.NotEmpty;

public record GenreAjoutDTO(@NotEmpty String libelle) {
}
