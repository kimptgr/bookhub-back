package fr.eni.bookhub.exception;

public class ElementDejaExistantException extends RuntimeException {
    public ElementDejaExistantException(String idLivre) {
        super("Livre déjà existant: " + idLivre);
    }
}
