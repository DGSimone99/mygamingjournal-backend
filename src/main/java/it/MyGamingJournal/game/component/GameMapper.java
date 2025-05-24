package it.MyGamingJournal.game.component;

import it.MyGamingJournal.game.entity.Game;

public class GameMapper {
    public static GameResponse toResponse(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId(game.getId());
        gameResponse.setName(game.getName());
        gameResponse.setSlug(game.getSlug());
        gameResponse.setBackgroundImage(game.getBackgroundImage());
        gameResponse.setReleased(game.getReleased());
        gameResponse.setRating(game.getRating());
        gameResponse.setParentPlatforms(game.getParentPlatforms());
        gameResponse.setDevelopers(game.getDevelopers());
        return gameResponse;
    }
}
