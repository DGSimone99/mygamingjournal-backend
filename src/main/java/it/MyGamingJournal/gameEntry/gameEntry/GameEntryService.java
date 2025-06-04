package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserService;
import it.MyGamingJournal.game.component.game.AchievementRepository;
import it.MyGamingJournal.game.component.game.GameRepository;
import it.MyGamingJournal.game.component.game.GameService;
import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserService userService;

    public List<GameEntry> getGamesByUser(User user) {
        List<GameEntry> gameEntries = gameEntryRepository.findByUser(user);
        if (gameEntries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No games found for user");
        }
        return gameEntries;
    }

    public GameEntry getGameEntry(User user, long gameId) {
        GameEntry gameEntry = gameEntryRepository.findByUserAndId(user, gameId);
        if (gameEntry == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game entry not found");
        }
        return gameEntry;
    }

    public List<GameEntryResponse> getGameEntryResponse(User user) {
        List<GameEntry> gameEntries = getGamesByUser(user);
        return gameEntries.stream()
                .map(GameEntryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public GameEntry addGameEntry(User user, long gameId, GameEntryRequest gameEntryRequest) {
        boolean alreadyExists = gameEntryRepository.findByUser(user).stream()
                .anyMatch(entry -> entry.getGame().getId().equals(gameId));
        if (alreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already in user's list");
        }

        Game game = gameService.getDetailsGame(gameId);

        GameEntry gameEntry = GameEntryMapper.fromGame(game, user);
        gameEntry.setUser(user);
        gameEntry.setHoursPlayed(gameEntryRequest.getHoursPlayed() != null ? gameEntryRequest.getHoursPlayed() : 0.0);
        gameEntry.setPersonalRating(gameEntryRequest.getPersonalRating() != null ? Math.round(gameEntryRequest.getPersonalRating() * 10.0) / 10.0 : 0);
        gameEntry.setStatus(gameEntryRequest.getStatus());
        gameEntry.setNotes(gameEntryRequest.getNotes());

        Sort sortByNameAsc = Sort.by("name").ascending();
        List<Achievement> achievements = achievementRepository.findAchievementsByGame(game, sortByNameAsc);

        List<AchievementEntry> achievementEntries = achievements.stream()
                .map(achievement -> AchievementEntryMapper.fromAchievement(achievement, gameEntry))
                .collect(Collectors.toList());

        gameEntry.setAchievements(achievementEntries);

        GameEntry savedEntry = gameEntryRepository.save(gameEntry);

        updateGameStats(game);

        return savedEntry;
    }


    @Transactional
    public GameEntry updateGameEntry(User user, long gameId, GameEntryRequest gameEntryRequest) {
        GameEntry gameEntry = getGameEntry(user, gameId);

        gameEntry.setHoursPlayed( gameEntryRequest.getHoursPlayed() != null ?  gameEntryRequest.getHoursPlayed() : gameEntry.getHoursPlayed());
        gameEntry.setPersonalRating(gameEntryRequest.getPersonalRating() != null ? Math.round(gameEntryRequest.getPersonalRating() * 10.0) / 10.0 : gameEntry.getPersonalRating());
        gameEntry.setStatus(gameEntryRequest.getStatus() != null ? gameEntryRequest.getStatus() : gameEntry.getStatus());
        gameEntry.setNotes(gameEntryRequest.getNotes() != null ? gameEntryRequest.getNotes() : gameEntry.getNotes());

        GameEntry savedEntry = gameEntryRepository.save(gameEntry);

        updateGameStats(gameEntry.getGame());

        return savedEntry;
    }

    @Transactional
    public void deleteGameEntry(User user, long gameId) {
        GameEntry gameEntry = getGameEntry(user, gameId);
        gameEntryRepository.delete(gameEntry);
        updateGameStats(gameEntry.getGame());
    }

    private void updateGameStats(Game game) {
        int count = gameEntryRepository.countByGame(game);
        Double average = gameEntryRepository.averageRatingByGame(game);
        game.setAdded(count);
        game.setRating(average != null ? Math.round(average * 10.0) / 10.0 : 0.0);
        gameService.updateGameStats(game);
    }

    public List<Long> getGameEntryIdsByUser(User user) {
        return gameEntryRepository.findAllGameIdsByUser(user);
    }
}
