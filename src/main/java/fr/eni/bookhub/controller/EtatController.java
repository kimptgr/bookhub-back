package fr.eni.bookhub.controller;

import fr.eni.bookhub.entity.Etat;
import fr.eni.bookhub.service.EtatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/etats")
@RequiredArgsConstructor
public class EtatController {
    private final EtatService etatService;

    @GetMapping
    public ResponseEntity<List<Etat>> getAllEtats() {
        return ResponseEntity.ok(etatService.trouverTousLesEtats());
    }

}