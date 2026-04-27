package fr.eni.bookhub.exception.emprunt;

public class LivreDejaEmprunteException extends RuntimeException {
    public LivreDejaEmprunteException(String message) {
        super("Livre déjà emprunté: "+message);
    }
}
