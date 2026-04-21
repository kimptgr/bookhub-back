package fr.eni.bookhub.config;


import fr.eni.bookhub.service.UtilisateurService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


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
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // désactivé car on utilise JWT, pas les sessions
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // pas de session côté serveur
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/auth/**").permitAll() // inscription et login = public
                            .anyRequest().authenticated()               // tout le reste = faut être connecté
                    );
            return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider() {

            DaoAuthenticationProvider provider = new DaoAuthenticationProvider(utilisateurService);
            provider.setUserDetailsService(utilisateurService);
            provider.setPasswordEncoder(passwordEncoder());

        return provider;
        }


        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(); // BCrypt = algorithme sécurisé pour les mots de passe
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
                throws Exception {
            return config.getAuthenticationManager();
        }

}
