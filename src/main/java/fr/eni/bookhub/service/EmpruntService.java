package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.EmpruntDTO;
import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.emprunt.LivreDejaEmprunteException;
import fr.eni.bookhub.exception.emprunt.UtilisateurADuRetardException;
import fr.eni.bookhub.exception.emprunt.UtilisateurATropDEmpruntsException;
import fr.eni.bookhub.repository.EmpruntRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmpruntService {
    private final LivreService livreService;
    private final ReservationService reservationService;
    private final UtilisateurService utilisateurService;

    private final EmpruntRepository empruntRepository;

    private final int EMPRUNT_MAX = 3;

    @Transactional
    public void ajoutEmprunt(@Valid EmpruntDTO empruntDTO) {
        Utilisateur emprunteur = utilisateurService.loadUserById(empruntDTO.emprunteurId());

        Livre livre = livreService.chercheLivreParIdEtDisponible(empruntDTO.livreId());
        verifieLivrePasEmprunte(livre);
        verifieNombreEmprunt(emprunteur);
        verifiePasDeRetard(emprunteur);
        reservationService.updateReservationLieesAuLivre(livre, emprunteur);
        livreService.updateEtat(livre, Etat.Code.EMPRUNTE);
        var emprunt = new Emprunt();
        LocalDate now = LocalDate.now();
        emprunt.setLivre(livre);
        emprunt.setUtilisateur(emprunteur);
        emprunt.setDateEmprunt(now);
        emprunt.setDateRetourPrevisionnel(now.plusDays(14));
        empruntRepository.save(emprunt);
    }


    private void verifieNombreEmprunt(Utilisateur emprunteur) {
        int empruntsEnCours = empruntRepository.countByUtilisateurAndDateRetourEffectifIsNull(emprunteur);

        if (empruntsEnCours >= EMPRUNT_MAX) {
            throw new UtilisateurATropDEmpruntsException();
        }
    }

    /**
     * Si j'ai du retard je ne peux pas emprunter
     * @param emprunteur
     */
    private void verifiePasDeRetard(Utilisateur emprunteur) {
        LocalDate today = LocalDate.now();
        int livresEnRetard = empruntRepository
                .countByUtilisateurAndDateRetourEffectifIsNullAndDateRetourPrevisionnelIsBefore(emprunteur, today);
        if (livresEnRetard > 0)
            throw new UtilisateurADuRetardException(livresEnRetard + " livre(s) en retard");
    }

    private void verifieLivrePasEmprunte(Livre livre) {
        if (empruntRepository.existsByLivreAndDateRetourEffectifIsNull(livre)) throw new LivreDejaEmprunteException(livre.getId().toString());
    }
}
