package fr.eni.bookhub.exception;

public class EtatNonExistantException extends RuntimeException{
    public EtatNonExistantException(String message) {
        super(message);
    }
}
