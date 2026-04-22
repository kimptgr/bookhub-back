package fr.eni.bookhub.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> gestionDeLaValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(LivreDejaExistantException.class)
    public ResponseEntity<String> gereLivreDejaExistant (LivreDejaExistantException ex) {
        return ResponseEntity.status(409).body("Livre déjà existant: "+ex.getMessage());
    }

    @ExceptionHandler(EtatNonExistantException.class)
    public ResponseEntity<String> gereEtatNonExistant (EtatNonExistantException ex) {
        return ResponseEntity.status(400).body("Cet état n'existe pas: " + ex.getMessage());
    }

    @ExceptionHandler(GenresNonCorrespondantException.class)
    public ResponseEntity<String> gereGenreInexistant(GenresNonCorrespondantException ex) {
        return ResponseEntity.badRequest().body("Les genres ne correspondent pas.");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        return ResponseEntity.badRequest()
                .body(ex.getMessage());
    }
}