package fr.eni.bookhub.specification;

import fr.eni.bookhub.controller.dto.RechercheDTO;
import fr.eni.bookhub.entity.Auteur;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Genre;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.repository.LivreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class LivreSpecificationTests {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final LivreSpecification livreSpecification = new LivreSpecification();

    @Test
    void getSpecificationsForIsbn_DoitRetournerLeLivreAvecLeBonIsbn() {
        String isbnRecherche = "978-2-35294-637-3";

        var livreAttendu = Livre.builder()
                .isbn(isbnRecherche).titre("Puzzle").auteurs(List.of(creerAuteur("Thilliez")))
                .genres(List.of(creerGenre("Test_Thiller"))).synopsis("lorem").etat(creerEtat("Test_Disponible")).build();

        var autreLivre = Livre.builder()
                .isbn("9782824625805").titre("Les chats au coeur d'ambre").auteurs(List.of(creerAuteur("Roy")))
                .genres(List.of(creerGenre("Test_Roman"))).synopsis("lorem").etat(creerEtat("Test_Emprunte")).build();

        entityManager.persist(livreAttendu);
        entityManager.persist(autreLivre);
        entityManager.flush();

        List<Livre> resultat = livreRepository.findAll(
                livreSpecification.getSpecificationsForIsbn(isbnRecherche)
        );

        assertThat(resultat)
                .hasSize(1)
                .extracting(Livre::getIsbn)
                .containsExactly(isbnRecherche);
    }

    @Test
    void getSpecificationsForGenreOuEtatOuTitreOuNomAuteur_DoitRetournerUneListeDeLivreCorrespondantAuxCriteres() {
        String saisieRecherche = "hill";
        String libelleThriller = "Test_Thriller";
        String libelleMusique = "Test_Musique";
        String[] genres = {libelleThriller, libelleMusique};
        String libelleDisponibilite = "Test_Disponible";
        var rechercheDTO = new RechercheDTO(saisieRecherche, genres, libelleDisponibilite);

        var thilliez = creerAuteur("Thilliez");
        var thriller = creerGenre(libelleThriller);
        var disponible = creerEtat(libelleDisponibilite);

        var livreAttenduNomAuteur = Livre.builder()
                .isbn("978-2-35294-637-3").titre("Puzzle").auteurs(List.of(thilliez))
                .genres(List.of(thriller)).synopsis("lorem").etat(disponible).build();

        var livreDuMemeAuteurMaisPasDunGenreDemande = Livre.builder()
                .isbn("9782265144286").titre("1991").auteurs(List.of(thilliez))
                .genres(List.of(creerGenre("Test_Polar"))).synopsis("lorem").etat(disponible).build();

        var livreDuMemeAuteurMaisPasDispo = Livre.builder()
                .isbn("9782266270304").titre("Pandemia").auteurs(List.of(thilliez))
                .genres(List.of(thriller)).synopsis("lorem").etat(creerEtat("Test_Emprunte")).build();

        var livreAttenduTitre = Livre.builder()
                .isbn("9782492469091").titre("Chill").auteurs(List.of(creerAuteur("Horning")))
                .genres(List.of(creerGenre("Test_Musique"))).synopsis("lorem").etat(disponible).build();

        livreAttenduNomAuteur = entityManager.persist(livreAttenduNomAuteur);
        entityManager.persist(livreDuMemeAuteurMaisPasDunGenreDemande);
        entityManager.persist(livreDuMemeAuteurMaisPasDispo);
        livreAttenduTitre = entityManager.persist(livreAttenduTitre);
        entityManager.flush();

        List<Livre> resultat = livreRepository.findAll(
                livreSpecification.getSpecificationsForGenreOuEtatOuTitreOuNomAuteur(rechercheDTO)
        );

        assertThat(resultat)
                .hasSize(2)
                .contains(livreAttenduNomAuteur, livreAttenduTitre);
    }


    private Auteur creerAuteur(String nomAuteur) {
        var auteur = new Auteur();
        auteur.setNom(nomAuteur);
        return entityManager.persist(auteur);
    }

    private Genre creerGenre(String libelle) {
        var genre = new Genre();
        genre.setLibelle(libelle);
        return entityManager.persist(genre);
    }

    private Etat creerEtat(String libelle) {
        var etat = new Etat();
        etat.setLibelle(libelle);
        return entityManager.persist(etat);
    }

//    private Avis creerAvis() {
//        var lectrice = new Utilisateur();
//        lectrice.setNom("Tartine");
//        lectrice.setPrenom("Marie");
//        lectrice.setEmail("mtartine@mail.com");
//        lectrice.setMotDePasseChiffre("fauxMDPquinestpasHashe");
//        lectrice.setRole(Utilisateur.Role.UTILISATEUR);
//        lectrice.setDesactive(false);
//        lectrice = entityManager.persist(lectrice);
//
//        var avis = new Avis();
//        avis.setCommentaire("J'adore");
//        avis.setNote((byte) 10);
//        avis.setLecteur(lectrice);
//        return entityManager.persist(avis);
//    }
}
