package fr.eni.bookhub.exception;

public class LivreDejaExistantException extends RuntimeException {
    public LivreDejaExistantException(String idLivre) {
        super("Livre déjà existant: " + idLivre);
    }
}
