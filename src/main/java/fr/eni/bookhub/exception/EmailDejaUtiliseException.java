package fr.eni.bookhub.exception;

public class EmailDejaUtiliseException extends RuntimeException {
    public EmailDejaUtiliseException(String email) {
        super("L'adresse email : " + email + " est déjà utilisée");
    }
}