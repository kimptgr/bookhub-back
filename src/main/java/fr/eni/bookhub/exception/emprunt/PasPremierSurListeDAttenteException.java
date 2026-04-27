package fr.eni.bookhub.exception.emprunt;

public class PasPremierSurListeDAttenteException extends RuntimeException {
    public PasPremierSurListeDAttenteException() {
        super("Cet emprunteur n'est pas le premier sur la liste d'attente");
    }
}
