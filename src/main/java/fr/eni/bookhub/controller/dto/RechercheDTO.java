package fr.eni.bookhub.controller.dto;

import fr.eni.bookhub.entity.Etat;

public record RechercheDTO(String saisie, String[] libellesGenres, Etat.Code[] libellesEtats) {
}
