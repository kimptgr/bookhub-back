package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.exception.GenresNonCorrespondantException;
import fr.eni.bookhub.exception.LivreDejaExistantException;
import fr.eni.bookhub.exception.LivreNotFoundException;
import fr.eni.bookhub.mapper.LivreMapper;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.specification.LivreSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static fr.eni.bookhub.utils.TextFormatter.isbnFormatter;

@Validated
@Service
@RequiredArgsConstructor
public class LivreService {
    private final LivreMapper livreMapper;

    private final AuteurService auteurService;
    private final GenreService genreService;
    private final EtatService etatService;

    private final LivreRepository livreRepository;

    @Transactional
    public void ajoutLivre(@NotNull LivreDTO livreDTO) {

        if (livreRepository.findByIsbn(isbnFormatter(livreDTO.isbn())).isPresent()) {
            throw new LivreDejaExistantException(livreDTO.isbn());
        }

        Livre livre = livreMapper.toEntity(livreDTO);
        List<Auteur> auteurs = auteurService.retrouverAuteurs(livreDTO.auteurs());
        livre.setAuteurs(auteurs);
        List<Genre> genres = genreService.retrouverGenres(livreDTO.genres());
        if (genres.size() != livreDTO.genres().size())
            throw new GenresNonCorrespondantException("");

        livre.setGenres(genres);
        livre.setEtat(etatService.retrouveEtat(livreDTO.idEtat()));

        livreRepository.save(livre);
    }

    public Page<Livre> rechercheLivres(RechercheDTO rechercheDTO, int numeroPage, int taillePage) {

        Specification<Livre> livreSpecification;
        PageRequest pageRequest;

        // Cas où on a saisi un ISBN, on ignore les filtres et les éléments de pagination
        if (rechercheDTO.saisie().matches("^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$")) {
            livreSpecification = LivreSpecification.getSpecificationsForIsbn(rechercheDTO.saisie());
            pageRequest = PageRequest.of(0, 20);

        } else {
            // Cas où on recherche par titre ou auteur·ice, on applique les filtres de genre et d'état
            livreSpecification = LivreSpecification.getSpecificationsForGenreOuEtatOuTitreOuNomAuteur(rechercheDTO);
            pageRequest = PageRequest.of(numeroPage, taillePage, Sort.by("titre").ascending());
        }

        return livreRepository.findAll(livreSpecification, pageRequest);
    }

    public Livre chercheLivreParId(Long id) {
        return livreRepository.findById(id).orElseThrow(()-> new LivreNotFoundException(id));
    }
}
