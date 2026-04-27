package fr.eni.bookhub.controller;

import fr.eni.bookhub.controller.dto.EmpruntDTO;
import fr.eni.bookhub.service.EmpruntService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/emprunts")
@RequiredArgsConstructor
public class EmpruntController {
    private final EmpruntService empruntService;

    @PostMapping
    public ResponseEntity<Void> emprunterLivre(@Valid @RequestBody EmpruntDTO empruntDTO) {
        empruntService.ajoutEmprunt(empruntDTO);
        return ResponseEntity.status(201).build();
    }
}
