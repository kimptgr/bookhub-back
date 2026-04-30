package fr.eni.bookhub.config;


import fr.eni.bookhub.service.UtilisateurService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UtilisateurService utilisateurService;
    private final JwtFilter jwtFilter;

    public SecurityConfig(UtilisateurService utilisateurService, JwtFilter jwtFilter) {
        this.utilisateurService = utilisateurService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // désactivé car on utilise JWT, pas les sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // pas de session côté serveur
                .authorizeHttpRequests(auth -> auth
                // Visiteur
                .requestMatchers(HttpMethod.POST, "/auth/inscription").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/connexion").permitAll()

                // Utilisateur connecté
                .requestMatchers(HttpMethod.GET, "/books/search").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.POST, "/books/{id}/avis").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/books/{id}").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/etats").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/genres").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers("/profil/me/**").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/emprunts/me").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/emprunts/me/historique").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/reservations/me/profil").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.PATCH, "/profil/me/mot-de-passe").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.POST, "/reservations").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.DELETE, "/reservations/*").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/reservations/me").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/emprunts/me").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/emprunts/me/historique").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/dashboard/me").hasAnyRole("UTILISATEUR", "BIBLIOTHECAIRE", "ADMINISTRATEUR")

                // Bibliothécaire
                .requestMatchers(HttpMethod.POST, "/books").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.PATCH, "/books/{id}").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.DELETE, "/books/{id}").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.POST, "/genres").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.POST, "/emprunts").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.PATCH, "/emprunts/{id}").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/dashboard/bibliothecaire").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/utilisateurs").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/refresh").hasAnyRole("BIBLIOTHECAIRE", "ADMINISTRATEUR")

                // Administrateur uniquement
                .requestMatchers(HttpMethod.GET, "/profils").hasRole("ADMINISTRATEUR")
                .requestMatchers(HttpMethod.GET, "/profils/{id}").hasRole("ADMINISTRATEUR")
                .requestMatchers(HttpMethod.POST, "/profils").hasRole("ADMINISTRATEUR")
                .requestMatchers(HttpMethod.PUT, "/profils/{id}").hasRole("ADMINISTRATEUR")
                .requestMatchers(HttpMethod.DELETE, "/profils/{id}").hasRole("ADMINISTRATEUR")
                .requestMatchers("/dashboard/admin/**").hasRole("ADMINISTRATEUR")

                .anyRequest().denyAll() // On rejette toutes les requêtes qui n'ont pas été explicitement autorisées
)
                .authenticationProvider(authenticationProvider())             // (3) on branche notre provider
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // (4) on insère le filtre JWT

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(utilisateurService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt = algorithme sécurisé pour les mots de passe
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) {
        return config.getAuthenticationManager();
    }

    //Configuration CORS pour autoriser Angular (http://localhost:4200)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


}
