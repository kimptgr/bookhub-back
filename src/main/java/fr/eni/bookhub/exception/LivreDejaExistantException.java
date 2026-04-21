package fr.eni.bookhub.exception;

public class LivreDejaExistantException extends RuntimeException {
    public LivreDejaExistantException(String message) {
        super(message);
    }
}
