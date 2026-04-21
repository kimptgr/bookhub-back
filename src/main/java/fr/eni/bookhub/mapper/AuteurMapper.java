package fr.eni.bookhub.mapper;

import fr.eni.bookhub.controller.dto.AuteurDTO;
import fr.eni.bookhub.entity.Auteur;
import org.springframework.stereotype.Component;

import static fr.eni.bookhub.utils.TextFormatter.convertToTitleCase;

@Component
public class AuteurMapper {
    public Auteur toEntity(AuteurDTO dto) {
        return new Auteur(dto.nomAuteur().toUpperCase(), convertToTitleCase(dto.prenomAuteur()));
    }
}
