package fr.eni.bookhub.exception;

public class UtilisateurATropDeReservationsException extends RuntimeException {
    public UtilisateurATropDeReservationsException(String email) {
        super("Nombre maximum de réservation atteint");
    }
}