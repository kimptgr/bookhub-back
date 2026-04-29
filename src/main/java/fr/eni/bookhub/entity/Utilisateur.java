package fr.eni.bookhub.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Utilisateur implements Serializable , UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String prenom;

    // String pour être sur de garder le 0 devant et varchar(20) si les gens mettent des point, des tirets ou que numéro international
    private String telephone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String motDePasseChiffre;

    @ToString.Exclude
    @OneToMany(mappedBy = "utilisateur")
    private List<Reservation> reservations;

    @ToString.Exclude
    @OneToMany(mappedBy = "utilisateur")
    private List<Emprunt> emprunts;

    @OneToMany(mappedBy = "lecteur")
    private List<Avis> avis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean desactive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return motDePasseChiffre;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !desactive;
    }

    public enum Role {
        UTILISATEUR,
        BIBLIOTHECAIRE,
        ADMINISTRATEUR
    }

}
