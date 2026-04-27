package fr.eni.bookhub.service;

import fr.eni.bookhub.controller.dto.UtilisateurResponseDTO;
import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.exception.ElementNotFoundException;
import fr.eni.bookhub.repository.UtilisateurRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé : " + email));
    }

    public Utilisateur loadUserById(Long id) {
        return utilisateurRepository.findByIdAndDesactiveIsFalse(id)
                .orElseThrow(() -> new ElementNotFoundException("Utilisateur non trouvé : " + id));
    }


    public @Nullable List<UtilisateurResponseDTO> recupererTousLesUtilisateursActifs() {
        return utilisateurRepository.findAllAndAndDesactiveIsFalse();
    }
}
