package fr.eni.bookhub.exception.emprunt;

public class UtilisateurATropDEmpruntsException extends RuntimeException {
    public UtilisateurATropDEmpruntsException() {
        super("Nombre maximum d'emprunts atteint");
    }
}