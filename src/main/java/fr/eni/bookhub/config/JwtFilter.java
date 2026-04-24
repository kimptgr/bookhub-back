package fr.eni.bookhub.config;

import fr.eni.bookhub.service.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter garantit que ce filtre s'exécute UNE SEULE fois par requête
    // Spring peut parfois appeler les filtres plusieurs fois, cette classe l'empêche

    private final JwtUtils jwtUtils;
    private final UtilisateurService utilisateurService;

    public JwtFilter(JwtUtils jwtUtils, UtilisateurService utilisateurService) {
        this.jwtUtils = jwtUtils;
        this.utilisateurService = utilisateurService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. On lit l'en-tête "Authorization" de la requête HTTP
        // Le frontend envoie : Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        String authHeader = request.getHeader("Authorization");

        // 2. Si l'en-tête est absent ou ne commence pas par "Bearer ", on laisse passer
        // sans authentifier (Spring Security refusera les routes protégées lui-même)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. On extrait le token en retirant le préfixe "Bearer " (7 caractères)
        String token = authHeader.substring(7);

        // 4. On extrait l'email contenu dans le token
        String email = jwtUtils.extractEmail(token);

        // 5. Si l'email est valide ET que l'utilisateur n'est pas déjà authentifié
        // (SecurityContextHolder.getContext().getAuthentication() == null)
        // → on vérifie le token et on authentifie l'utilisateur
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. On charge l'utilisateur depuis la base de données
            UserDetails userDetails = utilisateurService.loadUserByUsername(email);

            // 7. On vérifie que le token est valide (non expiré, signature correcte)
            if (jwtUtils.isTokenValid(token)) {

                // 8. On crée un objet d'authentification que Spring Security comprend
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,        // qui est l'utilisateur
                                null,               // ses credentials (plus nécessaires ici)
                                userDetails.getAuthorities() // ses rôles
                        );

                // 9. On ajoute les détails de la requête (IP, session...) à l'authentification
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 10. On dit à Spring Security "cet utilisateur est authentifié"
                // Toutes les routes protégées vont maintenant le laisser passer
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. On passe la requête au composant suivant dans la chaîne de filtres
        filterChain.doFilter(request, response);
    }
}