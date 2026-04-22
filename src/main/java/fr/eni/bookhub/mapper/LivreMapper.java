package fr.eni.bookhub.mapper;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.entity.Livre;
import org.springframework.stereotype.Component;

import static fr.eni.bookhub.utils.TextFormatter.isbnFormatter;

@Component
public class LivreMapper {

    public Livre toEntity(LivreDTO dto) {
        Livre livre = new Livre();
        livre.setIsbn(isbnFormatter(dto.isbn()));
        livre.setTitre(dto.titre());
        livre.setDateDeParution(dto.dateDeParution());
        livre.setSynopsis(dto.synopsis());
        livre.setUrlImage(dto.urlImage());
        return livre;
    }

}
