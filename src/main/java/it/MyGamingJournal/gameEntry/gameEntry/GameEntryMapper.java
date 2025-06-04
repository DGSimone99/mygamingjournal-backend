package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntryMapper;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntryResponse;
import it.MyGamingJournal.gameEntry.enums.GameStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class GameEntryMapper {
    public static GameEntry fromGame(Game game, User user) {
        return GameEntry.builder()
                .game(game)
                .user(user)
                .hoursPlayed(0.0)
                .personalRating(null)
                .status(GameStatus.PLAYING)
                .availableToPlay(false)
                .availableLanguages(new ArrayList<>())
                .availablePlatforms(new HashSet<>())
                .availableUntil(null)
                .notes("")
                .achievements(new ArrayList<>())
                .build();
    }

    public static GameEntryResponse toResponse(GameEntry gameEntry) {
        if (gameEntry == null) {
            return null;
        }

        List<AchievementEntryResponse> achievementResponses = gameEntry.getAchievements() == null
                ? Collections.emptyList()
                : gameEntry.getAchievements()
                .stream()
                .map(AchievementEntryMapper::toResponse)
                .collect(Collectors.toList());

        return GameEntryResponse.builder()
                .id(gameEntry.getId())
                .gameId(gameEntry.getGame() != null ? gameEntry.getGame().getId() : null)
                .gameName(gameEntry.getGame() != null ? gameEntry.getGame().getName() : null)
                .gameSlug(gameEntry.getGame() != null ? gameEntry.getGame().getSlug() : null)
                .backgroundImage(gameEntry.getGame() != null ? gameEntry.getGame().getBackgroundImage() : null)
                .hoursPlayed(gameEntry.getHoursPlayed())
                .personalRating(gameEntry.getPersonalRating())
                .status(gameEntry.getStatus())
                .notes(gameEntry.getNotes())
                .achievements(achievementResponses)
                .availableToPlay(gameEntry.isAvailableToPlay())
                .availableLanguages(gameEntry.getAvailableLanguages())
                .availablePlatforms(gameEntry.getAvailablePlatforms())
                .availableUntil(gameEntry.getAvailableUntil())
                .build();
    }


}

