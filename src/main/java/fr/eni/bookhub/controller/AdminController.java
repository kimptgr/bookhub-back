package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.UpdateRoleDTO;
import fr.eni.bookhub.controller.dto.UpdateUtilisateurAdminDTO;
import fr.eni.bookhub.controller.dto.UtilisateurAdminDTO;
import fr.eni.bookhub.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Récupère l'email de l'admin connecté depuis son token JWT
    private String getEmailAdmin(Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("/utilisateurs")
    public ResponseEntity<List<UtilisateurAdminDTO>> getUtilisateurs(Authentication authentication) {
        return ResponseEntity.ok(adminService.getTousLesUtilisateurs(getEmailAdmin(authentication)));
    }

    @PatchMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> modifierUtilisateur(
            @PathVariable Long id,
            @RequestBody UpdateUtilisateurAdminDTO dto,
            Authentication authentication) {
        adminService.modifierUtilisateur(id, dto, getEmailAdmin(authentication));
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/utilisateurs/roles")
    public ResponseEntity<Void> modifierRolesEnMasse(
            @RequestBody List<UpdateRoleDTO> updates,
            Authentication authentication) {
        adminService.modifierRolesEnMasse(updates, getEmailAdmin(authentication));
        return ResponseEntity.ok().build();
    }

}
