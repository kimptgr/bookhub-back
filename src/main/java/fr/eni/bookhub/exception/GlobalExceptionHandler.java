package fr.eni.bookhub.exception;

import fr.eni.bookhub.exception.emprunt.LivreDejaEmprunteException;
import fr.eni.bookhub.exception.emprunt.PasPremierSurListeDAttenteException;
import fr.eni.bookhub.exception.emprunt.UtilisateurADuRetardException;
import fr.eni.bookhub.exception.emprunt.UtilisateurATropDEmpruntsException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> gestionDeLaValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.error(errors.toString());
        return ResponseEntity.status(400).body(new APIError("400", errors.toString(), Instant.now()));
    }

    @ExceptionHandler(ElementDejaExistantException.class)
    public ResponseEntity<APIError> gereLivreDejaExistant(ElementDejaExistantException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(409).body(
                new APIError("409", ex.getMessage(), Instant.now()
                ));
    }


    @ExceptionHandler(GenresNonCorrespondantException.class)
    public ResponseEntity<APIError> gereGenreInexistant(GenresNonCorrespondantException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(400).body(new APIError("400", ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIError> handleConstraintViolation(
            ConstraintViolationException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(400).body(new APIError("400", ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<APIError> handleElementNotFound(
            ElementNotFoundException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(404).body(new APIError("404", ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(UtilisateurADejaReserveCelivreException.class)
    public ResponseEntity<APIError> handleResaYetPresent(
            UtilisateurADejaReserveCelivreException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(409).body(
                new APIError("409", ex.getMessage(), Instant.now()
                ));
    }

    @ExceptionHandler(UtilisateurATropDeReservationsException.class)
    public ResponseEntity<APIError> handleResaYetPresent(
            UtilisateurATropDeReservationsException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(409).body(
                new APIError("409", ex.getMessage(), Instant.now()
                ));
    }

    @ExceptionHandler(GenreDejaExistantException.class)
    public ResponseEntity<APIError> handleGenreDejaPresent(
            GenreDejaExistantException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(200).body(
                new APIError("200", ex.getMessage(), Instant.now()
                ));
    }

    @ExceptionHandler(UtilisateurADuRetardException.class)
    public ResponseEntity<APIError> handleRetard(UtilisateurADuRetardException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(418).body(
                new APIError("418", ex.getMessage(), Instant.now()
                ));
    }

    @ExceptionHandler(LivreDejaEmprunteException.class)
    public ResponseEntity<APIError> handleRetard(LivreDejaEmprunteException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(401).body(
                new APIError("401", ex.getMessage(), Instant.now()
                ));
    }

    @ExceptionHandler(UtilisateurATropDEmpruntsException.class)
    public ResponseEntity<APIError> handleTropDEmprunt(
            UtilisateurATropDEmpruntsException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(409).body(
                new APIError("409", ex.getMessage(), Instant.now()
                ));
    }

    @ExceptionHandler(PasPremierSurListeDAttenteException.class)
    public ResponseEntity<APIError> handleTropDEmprunt(
            PasPremierSurListeDAttenteException ex
    ) {
        log.error(ex.getMessage());
        return ResponseEntity.status(401).body(
                new APIError("401", ex.getMessage(), Instant.now()
                ));
    }

}