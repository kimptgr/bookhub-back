package fr.eni.bookhub.service;

import fr.eni.bookhub.entity.Utilisateur;
import fr.eni.bookhub.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé : " + id));
    }

}
