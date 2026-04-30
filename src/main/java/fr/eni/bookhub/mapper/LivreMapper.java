package fr.eni.bookhub.mapper;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.controller.dto.UpdateLivreDTO;
import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.repository.view.LivreView;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static fr.eni.bookhub.utils.TextFormatter.isbnFormatter;

@Component
public class LivreMapper {

    public Livre toEntity(LivreDTO dto) {
        Livre livre = new Livre();
        livre.setIsbn(isbnFormatter(dto.isbn()));
        livre.setTitre(dto.titre());
        livre.setDateDeParution(dto.dateDeParution());
        livre.setSynopsis(dto.synopsis());
        if (dto.urlImage() == null || dto.urlImage().isEmpty()) {
            livre.setUrlImage("https://picsum.photos/200");
        }
            livre.setUrlImage(dto.urlImage());

        return livre;
    }

    public void updateEntity(UpdateLivreDTO dto, Livre livre) {
        if (dto.isbn() != null) {
            livre.setIsbn(isbnFormatter(dto.isbn()));
        }
        if (dto.titre() != null) {
            livre.setTitre(dto.titre());
        }
        if (dto.dateDeParution() != null) {
            livre.setDateDeParution(dto.dateDeParution());
        }
        if (dto.synopsis() != null) {
            livre.setSynopsis(dto.synopsis());
        }
        if (dto.urlImage() != null) {
            livre.setUrlImage(dto.urlImage());
        }
    }

    public LivreView toLivreView(Livre livre) {
        Optional<Emprunt> empruntOpt = livre.getEmprunts().stream()
                .filter(emprunt -> emprunt.getDateRetourEffectif() == null).findAny();

        return new LivreView(
                livre.getId(),
                livre.getIsbn(),
                livre.getTitre(),
                livre.getAuteurs(),
                livre.getDateDeParution(),
                livre.getGenres(),
                livre.getSynopsis(),
                livre.getEtat(),
                // On remonte l'id de l'emprunt actif, ça part du principe qu'on a qu'un seul emprunt sans date de retour par livre => c'est fragile
                empruntOpt.map(Emprunt::getId).orElse(null),
                empruntOpt.map(emprunt -> emprunt.getUtilisateur().getId()).orElse(null),
                livre.getAvis(),
                livre.getUrlImage()
        );
    }

}
