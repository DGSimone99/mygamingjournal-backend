package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.exceptions.GameNotFoundException;
import it.MyGamingJournal.game.component.AchievementRepository;
import it.MyGamingJournal.game.component.GameRepository;
import it.MyGamingJournal.game.component.GameService;
import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.enums.CompletionMode;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GameEntryService {
    @Autowired
    private GameEntryRepository gameEntryRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    public List<GameEntry> getGamesByUser(AppUser user) {
        List<GameEntry> gameEntries = gameEntryRepository.findByUser(user);
        if(gameEntries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No games found");
        }
        return gameEntries;
    }

    public GameEntry addGameEntry(long idGame, AppUser user, Double hoursPlayed, Double personalRating, GameStatus status, CompletionMode completionMode, String notes) {
            boolean alreadyExists = gameEntryRepository.findByUser(user).stream()
                    .anyMatch(entry -> entry.getGameEntryId().equals(idGame));
            if (alreadyExists) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already in user's list");
            }

            Game game = gameService.getDetailsGame(idGame);
            GameEntry gameEntry = new GameEntry();
            gameEntry.setGame(game);
            gameEntry.setGameEntryId(idGame);
            gameEntry.setGameName(game.getName());
            gameEntry.setGameSlug(game.getSlug());
            gameEntry.setUser(user);
            gameEntry.setHoursPlayed(hoursPlayed);
            gameEntry.setPersonalRating(personalRating);
            gameEntry.setStatus(status);
            gameEntry.setCompletionMode(completionMode);
            gameEntry.setNotes(notes);

             Sort sort = Sort.by("name").ascending();
            List<Achievement> achievements = achievementRepository.findAchievementsByGame(game, sort);

            List<AchievementEntry> entries = achievements.stream()
                    .map(achievement -> {
                        AchievementEntry achievementEntry = new AchievementEntry();
                        achievementEntry.setGameEntry(gameEntry);
                        achievementEntry.setAchievementId(achievement.getId());
                        achievementEntry.setName(achievement.getName());
                        achievementEntry.setDescription(achievement.getDescription());
                        achievementEntry.setImage(achievement.getImage());
                        achievementEntry.setAveragePercentage(achievement.getAveragePercentage());
                        achievementEntry.setUnlocked(false);
                        return achievementEntry;
                    }).toList();
            gameEntry.setAchievements(entries);

        GameEntry savedEntry = gameEntryRepository.save(gameEntry);

        int actualCount = gameEntryRepository.countByGame(game);
        Double average = gameEntryRepository.findAverageRatingByGame(game);

        game.setAdded(actualCount);
        game.setAverageRating(average != null ? average : 0.0);

        gameService.updateGameStats(game);

        return savedEntry;
    }

    public GameEntry updateGameEntry(long idGame, AppUser user, Double hoursPlayed, Double personalRating, GameStatus status, CompletionMode completionMode, String notes) {
        GameEntry gameEntry = gameEntryRepository.findByUserAndGameId(user, idGame);

        if (gameEntry == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gioco non presente nella tua lista");
        }

        Game game = gameService.getDetailsGame(idGame);
        gameEntry.setHoursPlayed(hoursPlayed);
        gameEntry.setPersonalRating(personalRating);
        gameEntry.setStatus(status);
        gameEntry.setCompletionMode(completionMode);
        gameEntry.setNotes(notes);
        GameEntry savedEntry = gameEntryRepository.save(gameEntry);

        int actualCount = gameEntryRepository.countByGame(game);
        Double average = gameEntryRepository.findAverageRatingByGame(game);

        game.setAdded(actualCount);
        game.setAverageRating(average != null ? average : 0.0);

        gameService.updateGameStats(game);

        return savedEntry;
    }

    public void deleteGameEntry(long idGame, AppUser user) {
        GameEntry gameEntry = gameEntryRepository.findByUserAndGameId(user, idGame);
        if (gameEntry == null) {
            throw new GameNotFoundException(idGame);
        }
        gameEntryRepository.delete(gameEntry);
    }

    public void setAvailability(GameEntry gameEntry, Set<String> languages, AppUser currentUser) {
        if (!gameEntry.getUser().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this game entry");
        }
        gameEntry.setAvailableLanguages(languages != null ? languages : new HashSet<>());
        gameEntry.setAvailableToPlay(true);
        gameEntry.setAvailableUntil(LocalDate.now().plusWeeks(2));
        gameEntryRepository.save(gameEntry);
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Rome")
    public void expireGameEntryAvailability() {
        List<GameEntry> expired = gameEntryRepository.findByAvailableToPlayTrueAndAvailableUntilBefore(LocalDate.now());
        for (GameEntry entry : expired) {
            entry.setAvailableToPlay(false);
            entry.setAvailableLanguages(Collections.emptySet());
            entry.setAvailableUntil(null);
        }
        gameEntryRepository.saveAll(expired);
    }
}
