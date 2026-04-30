package fr.eni.bookhub.mapper;

import fr.eni.bookhub.controller.dto.EmpruntMisAJourDTO;
import fr.eni.bookhub.entity.Emprunt;
import org.springframework.stereotype.Component;

@Component
public class EmpruntMapper {
    public EmpruntMisAJourDTO toEmpruntMisAJourDTO(Emprunt emprunt) {
        return new EmpruntMisAJourDTO(
                emprunt.getId(),
                emprunt.getLivre().getId(),
                emprunt.getUtilisateur().getId(),
                emprunt.getDateEmprunt(),
                emprunt.getDateRetourPrevisionnel(),
                emprunt.getDateRetourEffectif()
        );
    }
}
