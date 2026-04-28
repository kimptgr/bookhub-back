package fr.eni.bookhub.exception.reservation;

public class UtilisateurQuiEmprunteNePeutPasReserverException extends RuntimeException {
    public UtilisateurQuiEmprunteNePeutPasReserverException() {
        super("Un utilisateur qui a un emprunt en cours sur un livre ne peut pas le réserver.");
    }
}
