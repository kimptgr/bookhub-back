package fr.eni.bookhub.exception;

public class UtilisateurADejaReserveCelivreException extends RuntimeException {
    public UtilisateurADejaReserveCelivreException() {
        super("Réservation déjà existante.");
    }
}