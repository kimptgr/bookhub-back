package fr.eni.bookhub.exception;

public class GenresNonCorrespondantException extends RuntimeException {
    public GenresNonCorrespondantException() {
        super("Les genres ne correspondent pas.");
    }
}
