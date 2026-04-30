package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.*;
import fr.eni.bookhub.entity.*;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.exception.IdDiscordantsException;
import fr.eni.bookhub.exception.emprunt.LivreDejaEmprunteException;
import fr.eni.bookhub.exception.emprunt.UtilisateurADuRetardException;
import fr.eni.bookhub.exception.emprunt.UtilisateurATropDEmpruntsException;
import fr.eni.bookhub.mapper.EmpruntMapper;
import fr.eni.bookhub.repository.EmpruntRepository;
import fr.eni.bookhub.repository.EtatRepository;
import fr.eni.bookhub.repository.LivreRepository;
import fr.eni.bookhub.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class EmpruntService {
    private final LivreService livreService;
    private final ReservationService reservationService;
    private final UtilisateurService utilisateurService;

    private final EmpruntMapper empruntMapper;

    private final EmpruntRepository empruntRepository;
    private final EtatRepository etatRepository;
    private final LivreRepository livreRepository;
    private final ReservationRepository reservationRepository;

    private final int EMPRUNT_MAX = 3;

    @Transactional
    public void ajoutEmprunt(@Valid EmpruntDTO empruntDTO) {
        Utilisateur emprunteur = utilisateurService.loadUserById(empruntDTO.emprunteurId());

        Livre livre = livreService.chercheLivreParIdEtDisponibleOuReserve(empruntDTO.livreId());
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
     *
     */
    private void verifiePasDeRetard(Utilisateur emprunteur) {
        LocalDate today = LocalDate.now();
        int livresEnRetard = empruntRepository
                .countByUtilisateurAndDateRetourEffectifIsNullAndDateRetourPrevisionnelIsBefore(emprunteur, today);
        if (livresEnRetard > 0)
            throw new UtilisateurADuRetardException(livresEnRetard + " livre(s) en retard");
    }

    private void verifieLivrePasEmprunte(Livre livre) {
        if (empruntRepository.existsByLivreAndDateRetourEffectifIsNull(livre))
            throw new LivreDejaEmprunteException(livre.getId().toString());
    }

    /**
     * Pour le moment les cas prévus sont : rendre un livre, prolonger un emprunt
     *
     */
    @Transactional
    public EmpruntMisAJourDTO updateEmprunt(UpdateEmpruntDTO updateEmpruntDTO) {
        Emprunt emprunt = empruntRepository.findById(updateEmpruntDTO.idEmprunt())
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

            Livre livre = livreRepository.getReferenceById(updateEmpruntDTO.idLivre());
            List<Reservation> reservations = reservationRepository.findAllByLivreAndEstSupprimeeIsFalse(livre);

            Etat etat;
            // Si la réservation est vide, on rend le livre disponible
            if (reservations.isEmpty()) {
                etat = etatRepository.findByLibelle(Etat.Code.DISPONIBLE)
                        .orElseThrow(() -> new ElementNotFoundException("Erreur lors de la récupération des états"));

            // S'il y a des réservations en liste d'attente, on passe le livre en réservé
            } else {
                etat = etatRepository.findByLibelle(Etat.Code.RESERVE)
                        .orElseThrow(() -> new ElementNotFoundException("Erreur lors de la récupération des états"));
            }

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

    // Dans EmpruntService, ajoute ces deux méthodes

    public List<EmpruntEnCoursDTO> getEmpruntsEnCours(Utilisateur utilisateur) {
        LocalDate today = LocalDate.now();
        return empruntRepository
                .findByUtilisateurAndDateRetourEffectifIsNull(utilisateur)
                .stream()
                .map(e -> new EmpruntEnCoursDTO(
                        e.getId(),
                        e.getLivre().getTitre(),
                        e.getLivre().getUrlImage(),
                        e.getDateRetourPrevisionnel(),
                        e.getDateRetourPrevisionnel() != null
                        //Pour calculer le retard
                        && e.getDateRetourPrevisionnel().isBefore(today)
                ))
                .toList();
    }

    public List<EmpruntHistoriqueDTO> getHistoriqueEmprunts(Utilisateur utilisateur) {
        return empruntRepository
                .findByUtilisateurAndDateRetourEffectifIsNotNull(utilisateur)
                .stream()
                .map(e -> new EmpruntHistoriqueDTO(
                        e.getId(),
                        e.getLivre().getTitre(),
                        e.getLivre().getUrlImage(),
                        e.getDateEmprunt(),
                        e.getDateRetourEffectif()
                ))
                .toList();
    }


    /**
     * Récupère les emprunts en retard et met à jour leur état
     */
    @Transactional
    public void recupereLesEmpruntsEnRetard(LocalDate date) {
    List<Emprunt> retards = empruntRepository.findAllByDateRetourPrevisionnelBeforeAndDateRetourEffectifIsNull(date);

    //récupère le livre et met état à en retard
        for (Emprunt retard : retards) {
            String mail = retard.getUtilisateur().getEmail();
            String titre = retard.getLivre().getTitre();
            log.warn("L'utilisateur {} a du retard sur le livre {}.", mail, titre);
        }
    }
}
