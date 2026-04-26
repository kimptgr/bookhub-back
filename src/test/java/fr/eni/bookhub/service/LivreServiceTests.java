package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.mapper.LivreMapper;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.specification.LivreSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LivreServiceTests {

//    @Mock
//    private LivreMapper livreMapper;
//    @Mock
//    private AuteurService auteurService;
//    @Mock
//    private GenreService genreService;
    @Mock
    private EtatService etatService;
    @Mock
    private LivreRepository livreRepository;
    @Mock
    private LivreSpecification livreSpecification;

    @InjectMocks
    private LivreService livreService;

    @Test
    void rechercheLivres_QuandLaSaisieEstUnIsbn13_DoitAppelerGetSpecificationsForIsbn() {
        String saisie = "978-2-35294-637-3";
        var rechercheDTO = new RechercheDTO(saisie, new String[] { "sera", "ignore" }, "ça aussi");
        ArgumentCaptor<PageRequest> pageRequest = forClass(PageRequest.class);

        livreService.rechercheLivres(rechercheDTO, 42, 9000);

        verify(livreSpecification, times(1)).getSpecificationsForIsbn(saisie);

        // La pagination est ignorée pour la recherche par ISBN
        verify(livreRepository).findAll((Specification<Livre>) any(), pageRequest.capture());
        assertEquals(PageRequest.of(0, 20), pageRequest.getValue());
    }

    @Test
    void rechercheLivres_QuandLaSaisieEstUnIsbn10_DoitAppelerGetSpecificationsForIsbn() {
        String saisie = "0-19-853453";
        var rechercheDTO = new RechercheDTO(saisie, new String[] { "toujours", "ignore" }, "idem");
        ArgumentCaptor<PageRequest> pageRequest = forClass(PageRequest.class);

        livreService.rechercheLivres(rechercheDTO, 42, 9000);

        verify(livreSpecification, times(1)).getSpecificationsForIsbn(saisie);

        // La pagination est ignorée pour la recherche par ISBN
        verify(livreRepository).findAll((Specification<Livre>) any(), pageRequest.capture());
        assertEquals(PageRequest.of(0, 20), pageRequest.getValue());
    }

    @Test
    void rechercheLivres_QuandLaSaisieNestPasUnIsbn_DoitAppelerGetSpecificationsForGenreOuEtatOuTitreOuNomAuteur() {
        var rechercheDTO = new RechercheDTO("Thilliez", new String[] { "hors", "test" }, "désolé");
        int numeroPage = 3;
        int taillePage = 30;
        ArgumentCaptor<PageRequest> pageRequest = forClass(PageRequest.class);

        livreService.rechercheLivres(rechercheDTO, numeroPage, taillePage);

        verify(livreSpecification, times(1)).getSpecificationsForGenreOuEtatOuTitreOuNomAuteur(rechercheDTO);

        // La pagination est prise en compte pour les recherches hors ISBN
        verify(livreRepository).findAll((Specification<Livre>) any(), pageRequest.capture());
        assertEquals(PageRequest.of(numeroPage, taillePage, Sort.by("titre").ascending()), pageRequest.getValue());
    }
}
