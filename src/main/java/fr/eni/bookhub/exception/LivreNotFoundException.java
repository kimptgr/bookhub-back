package fr.eni.bookhub.exception;

public class LivreNotFoundException extends RuntimeException {
    public LivreNotFoundException(Long id) {
        super("Livre avec l'id " + id + " n'existe pas");
    }
}
