package it.MyGamingJournal.game.component.game;

import it.MyGamingJournal.game.entity.Game;

import java.util.ArrayList;

public class GameMapper {
    public static GameResponse toResponse(Game game) {
        return GameResponse.builder()
            .id(game.getId())
            .name(game.getName())
            .slug(game.getSlug())
            .backgroundImage(game.getBackgroundImage())
            .released(game.getReleased())
            .rating(game.getRating())
            .metacritic(game.getMetacritic())
            .parentPlatforms(game.getParentPlatforms() != null ? game.getParentPlatforms() : new ArrayList<>())
            .developers(game.getDevelopers() != null ? game.getDevelopers() : new ArrayList<>())
            .build();
    }
}
