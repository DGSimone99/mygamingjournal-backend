package it.MyGamingJournal.exceptions;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(Long id) {
        super("Game with ID " + id + " not found");
    }
}
