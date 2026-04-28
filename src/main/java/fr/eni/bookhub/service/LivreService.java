package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.LivreDTO;
import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.controller.dto.UpdateLivreDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.exception.GenresNonCorrespondantException;
import fr.eni.bookhub.exception.ElementDejaExistantException;
import fr.eni.bookhub.mapper.LivreMapper;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.specification.LivreSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.eni.bookhub.utils.TextFormatter.isbnFormatter;

@Validated
@Service
@RequiredArgsConstructor
public class LivreService {
    private final LivreMapper livreMapper;

    private final AuteurService auteurService;

    private final GenreService genreService;

    private final EtatService etatService;

    private final LivreSpecification livreSpecification;

    private final LivreRepository livreRepository;

    // Vérifie que la string ressemble à un ISBN, cette regex peut matcher avec des ISBN non valides
    private static final Pattern isbnPattern = Pattern.compile("[0-9\\- ]{10,17}X?");

    @Transactional
    public void ajoutLivre(@NotNull LivreDTO livreDTO) {

        verifierIsbnUnique(livreDTO.isbn(), null);

        Livre livre = livreMapper.toEntity(livreDTO);
        List<Auteur> auteurs = auteurService.retrouverAuteurs(livreDTO.auteurs());
        livre.setAuteurs(auteurs);
        List<Genre> genres = genreService.retrouverGenres(livreDTO.genres());
        if (genres.size() != livreDTO.genres().size())
            throw new GenresNonCorrespondantException();

        livre.setGenres(genres);
        livre.setEtat(etatService.retrouveEtat(livreDTO.idEtat()));

        livreRepository.save(livre);
    }

    @Transactional
    public void modifierLivre(UpdateLivreDTO livreDTO, Long id) {

        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Le livre avec l'id " + id + " n'a pas été trouvé ou n'existe pas"));

        if (livreDTO.isbn() != null) {
            verifierIsbnUnique(livreDTO.isbn(), id);
        }

        livreMapper.updateEntity(livreDTO, livre);

        if (livreDTO.auteurs() != null) {
            List<Auteur> auteurs = auteurService.retrouverAuteurs(livreDTO.auteurs());
            livre.setAuteurs(auteurs);
        }

        if (livreDTO.genres() != null) {
            List<Genre> genres = genreService.retrouverGenres(livreDTO.genres());
            if (genres.size() != livreDTO.genres().size())
                throw new GenresNonCorrespondantException();
            livre.setGenres(genres);
        }

        if (livreDTO.idEtat() != null) {
            livre.setEtat(etatService.retrouveEtat(livreDTO.idEtat()));
        }

        livreRepository.save(livre);

    }

    /*
    je vérifie si l'ISBN est unique
    dans le cas d'un livre ajouté, si on remonte un id de livre existant, on déclenche l'exception
    dans le cas d'un livre à modifier, si on remonte un id différent de celui que l'on modifie, on déclenche l'exception
    cela permet de vérifier, par exemple s'il y a eu une erreur de saisie d'ISBN et qu'il est modifié
     */
    private void verifierIsbnUnique(String isbn, Long idExclu) {
        livreRepository.findByIsbn(isbnFormatter(isbn))
                .filter(l -> idExclu == null || !l.getId().equals(idExclu))
                .ifPresent(l -> {
                    throw new ElementDejaExistantException(isbn);
                });
    }

    public void deleteLivre(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Le livre avec l'id " + id + " n'existe pas"));

        if (!livre.getReservations().isEmpty()) {
            throw new ElementDejaExistantException("Impossible de supprimer ce livre, il possède des réservations actives.");
        }

        livreRepository.deleteById(id);
    }



    public Page<Livre> rechercheLivres(RechercheDTO rechercheDTO, int numeroPage, int taillePage) {

        Specification<Livre> specification;
        PageRequest pageRequest;
        Matcher isbnMatcher = isbnPattern.matcher(rechercheDTO.saisie());

        // Cas où on a saisi un ISBN, on ignore les filtres et les éléments de pagination
        if (isbnMatcher.matches()) {
            specification = livreSpecification.getSpecificationsForIsbn(isbnFormatter(rechercheDTO.saisie()));
            pageRequest = PageRequest.of(0, 20);

        } else {
            // Cas où on recherche par titre ou auteur·ice, on applique les filtres de genre et d'état
            specification = livreSpecification.getSpecificationsForGenreOuEtatOuTitreOuNomAuteur(rechercheDTO);
            pageRequest = PageRequest.of(numeroPage, taillePage, Sort.by("titre").ascending());
        }

        return livreRepository.findAll(specification, pageRequest);
    }

    public Livre chercheLivreParId(Long id) {
        return livreRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Livre avec l'id " + id + " n'existe pas"));
    }

    public Livre chercheLivreParIdEtUtilisable(Long id) {
        return livreRepository.findByIdAndEtatLibelleNot(id, Etat.Code.INUTILISABLE).orElseThrow(() -> new ElementNotFoundException("Livre non trouvé (id: " + id + ")"));
    }

    public void updateEtat(Livre livre, Etat.Code etatLabel) {
        Etat etat = etatService.retrouveEtatParLibelle(etatLabel);
        livre.setEtat(etat);
        livreRepository.save(livre);
    }

    public Livre chercheLivreParIdEtDisponible(Long id) {
        return livreRepository.findByIdAndEtatLibelle(id, Etat.Code.DISPONIBLE).orElseThrow(() -> new ElementNotFoundException("Livre non trouvé (id: " + id + ")"));
    }
}
