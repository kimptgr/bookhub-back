package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.EmpruntDTO;
import fr.eni.bookhub.controller.dto.EmpruntMisAJourDTO;
import fr.eni.bookhub.controller.dto.UpdateEmpruntDTO;
import fr.eni.bookhub.entity.Emprunt;
import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.entity.Livre;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.exception.IdDiscordantsException;
import fr.eni.bookhub.exception.emprunt.LivreDejaEmprunteException;
import fr.eni.bookhub.exception.emprunt.UtilisateurADuRetardException;
import fr.eni.bookhub.exception.emprunt.UtilisateurATropDEmpruntsException;
import fr.eni.bookhub.mapper.EmpruntMapper;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.EtatRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmpruntService {
    private final LivreService livreService;
    private final ReservationService reservationService;
    private final UtilisateurService utilisateurService;

    private final EmpruntMapper empruntMapper;

    private final EmpruntRepository empruntRepository;
    private final EtatRepository etatRepository;

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

    /**
     * Pour le moment les cas prévus sont : rendre un livre, prolonger un emprunt
     * @param updateEmpruntDTO
     * @return
     */
    @Transactional
    public EmpruntMisAJourDTO updateEmprunt(UpdateEmpruntDTO updateEmpruntDTO) {
        Emprunt emprunt = empruntRepository.findById(updateEmpruntDTO.idResa())
                .orElseThrow(() -> new ElementNotFoundException("L'emprunt demandé n'a pas été trouvé"));

        if (!emprunt.getUtilisateur().getId().equals(updateEmpruntDTO.idEmprunteur())) {
            throw new IdDiscordantsException("Les deux id d'utilisateur fournis ne correspondent pas");
        }

        if (!emprunt.getLivre().getId().equals(updateEmpruntDTO.idLivre())) {
            throw new IdDiscordantsException("Les deux id de livre fournis ne correspondent pas");
        }

        if (emprunt.getDateRetourEffectif() == null
                && updateEmpruntDTO.dateRetour().isBefore(LocalDate.now().plusDays(1).atStartOfDay())) {
            // Si la date de retour portée par le DTO est avant demain, on considère qu'on veut rendre l'emprunt
            Etat etat = etatRepository.findByLibelle(Etat.Code.DISPONIBLE)
                    .orElseThrow(() -> new ElementNotFoundException("Erreur lors de la récupération des états"));

            emprunt.setDateRetourEffectif(LocalDate.now());
            emprunt.getLivre().setEtat(etat);
        }

        if (emprunt.getDateRetourEffectif() == null
                && updateEmpruntDTO.dateRetour().isAfter(LocalDate.now().plusDays(1).atStartOfDay())) {
            // Si la date de retour portée par le DTO est à demain où après, on considère qu'on veut prolonger l'emprunt
            emprunt.setDateRetourPrevisionnel(updateEmpruntDTO.dateRetour().toLocalDate());
        }

        return empruntMapper.toEmpruntMisAJourDTO(empruntRepository.save(emprunt));
    }
}
