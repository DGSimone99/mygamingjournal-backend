package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

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
        if(gameEntries.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"No games found");
        }
        return gameEntries;
    }

    public GameEntry getGameEntry(User user, long idGame) {
        GameEntry gameEntry = gameEntryRepository.findByUserAndGameId(user, idGame);
        if (gameEntry == null) {
            throw new GameNotFoundException(idGame);
        }
        return gameEntry;
    }

    public List <GameEntryResponse> getGameEntryResponse(User user) {
        List<GameEntry> gameEntries = getGamesByUser(user);
        return gameEntries.stream()
                .map(gameEntry -> new GameEntryResponse(gameEntry.getGame().getId()))
                .toList();
    }

    public GameEntry addGameEntry(User user, long idGame, Double hoursPlayed, Double personalRating, GameStatus status, CompletionMode completionMode, String notes) {
            boolean alreadyExists = gameEntryRepository.findByUser(user).stream()
                    .anyMatch(entry -> entry.getGame().getId().equals(idGame));
            if (alreadyExists) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Game already in user's list");
            }

            Game game = gameService.getDetailsGame(idGame);
            GameEntry gameEntry = new GameEntry();
            gameEntry.setGame(game);
            gameEntry.setRealGameId(game.getId());
            gameEntry.setGameName(game.getName());
            gameEntry.setGameSlug(game.getSlug());
            gameEntry.setBackgroundImage(game.getBackgroundImage());
            gameEntry.setUser(user);
            gameEntry.setHoursPlayed(hoursPlayed);
            gameEntry.setPersonalRating(Math.round(personalRating * 10.0) / 10.0);
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
        Double average = gameEntryRepository.averageRatingByGame(game);

        game.setAdded(actualCount);
        game.setRating(average != null ? Math.round(average * 10.0) / 10.0 : 0.0);

        gameService.updateGameStats(game);

        return savedEntry;
    }

    public GameEntry updateGameEntry(User user, long idGame, Double hoursPlayed, Double personalRating, GameStatus status, CompletionMode completionMode, String notes) {
        GameEntry gameEntry = getGameEntry(user, idGame);

        Game game = gameService.getDetailsGame(idGame);
        gameEntry.setHoursPlayed(hoursPlayed);
        gameEntry.setPersonalRating(Math.round(personalRating * 10.0) / 10.0);
        gameEntry.setStatus(status);
        gameEntry.setCompletionMode(completionMode);
        gameEntry.setNotes(notes);
        GameEntry savedEntry = gameEntryRepository.save(gameEntry);

        int actualCount = gameEntryRepository.countByGame(game);
        Double average = gameEntryRepository.averageRatingByGame(game);

        game.setAdded(actualCount);
        game.setRating(average != null ? Math.round(average * 10.0) / 10.0 : 0.0);

        gameService.updateGameStats(game);

        return savedEntry;
    }

    public void deleteGameEntry(User user, long id) {
        GameEntry gameEntry = gameEntryRepository.findByUserAndId(user, id);
        gameEntryRepository.delete(gameEntry);
        gameService.updateGameStats(gameEntry.getGame());
    }

    public Page<GameEntryAvailabilityResponse> getAvailablePlayers(
            long realGameId,
            Set<String> languages,
            Set<String> platforms,
            Pageable pageable) {

        Page<GameEntry> entries = gameEntryRepository.findAvailablePlayers(realGameId, languages, platforms, pageable);

        return entries.map(this::toAvailabilityDTO);
    }


    private GameEntryAvailabilityResponse toAvailabilityDTO(GameEntry entry) {
        return new GameEntryAvailabilityResponse(
                entry.getId(),
                entry.getGame().getName(),
                entry.getGame().getBackgroundImage(),
                entry.getUser().getDisplayName(),
                entry.getUser().getAvatarUrl(),
                entry.getUser().getId(),
                entry.getAvailableUntil(),
                entry.getAvailablePlatforms(),
                entry.getAvailableLanguages()
        );
    }

    public void updateAvailability(Long gameEntryId, GameEntryAvailabilityUpdateRequest request) {
        GameEntry entry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new RuntimeException("GameEntry not found"));

        entry.setAvailableToPlay(request.isAvailableToPlay());

        if (request.isAvailableToPlay()) {
            Set<String> requestedPlatforms = request.getAvailablePlatforms();
            if (requestedPlatforms == null || requestedPlatforms.isEmpty()) {
                throw new IllegalArgumentException("You must select at least one platform");
            }
            if (requestedPlatforms.size() > 3) {
                throw new IllegalArgumentException("You can select up to 3 platforms at a time");
            }

            Set<String> validPlatforms = new HashSet<>(entry.getGame().getPlatforms());
            if (!validPlatforms.containsAll(requestedPlatforms)) {
                throw new IllegalArgumentException("You can only select platforms that the game supports");
            }

            entry.setAvailablePlatforms(requestedPlatforms);
            entry.setAvailableUntil(LocalDate.now().plusDays(14));

            User user = entry.getUser();
            if (user == null || user.getLanguages() == null || user.getLanguages().isEmpty()) {
                throw new IllegalStateException("User languages not found");
            }

            entry.setAvailableLanguages(new ArrayList<>(user.getLanguages()));
        } else {
            entry.setAvailableUntil(null);
            entry.getAvailablePlatforms().clear();
            entry.getAvailableLanguages().clear();
        }

        gameEntryRepository.save(entry);
    }



    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Rome")
    public void expireGameEntryAvailability() {
        List<GameEntry> expired = gameEntryRepository.findByAvailableToPlayTrueAndAvailableUntilBefore(LocalDate.now());
        for (GameEntry entry : expired) {
            entry.setAvailableToPlay(false);
            entry.setAvailableLanguages(List.of());
            entry.setAvailableUntil(null);
        }
        gameEntryRepository.saveAll(expired);
    }
}
