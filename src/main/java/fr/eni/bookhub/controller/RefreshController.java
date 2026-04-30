package fr.eni.bookhub.controller;

import fr.eni.bookhub.Scheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RefreshController {
    private final Scheduler scheduler;
    @GetMapping("/refresh")
    public ResponseEntity<Void> test() {
        scheduler.chercheExpirationScheduled();
        scheduler.chercheEmpruntEnRetard();
        return ResponseEntity.ok().build();
    }
}
