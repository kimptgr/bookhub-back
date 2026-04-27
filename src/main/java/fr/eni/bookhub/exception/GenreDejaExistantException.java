package fr.eni.bookhub.exception;

public class GenreDejaExistantException extends RuntimeException {
  public GenreDejaExistantException() {
    super("Genre déjà existant");
  }
}
