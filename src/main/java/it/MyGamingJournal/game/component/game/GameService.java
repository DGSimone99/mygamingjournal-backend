package it.MyGamingJournal.game.component.game;

import it.MyGamingJournal.exceptions.GameNotFoundException;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GameService {

    private final GameRepository gameRepository;
    private final GameEntryRepository gameEntryRepository;

    public Game getDetailsGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new GameNotFoundException(id));
    }

    public Page<GameResponse> getGames(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> searchGamesByName(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        return gameRepository.findByNameContainingIgnoreCase(name.trim(), pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> getGamesByGenre(String genre, Pageable pageable) {
        if (genre == null || genre.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        return gameRepository.findByGenre(genre.trim(), pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> getGamesByDeveloper(String developer, Pageable pageable) {
        if (developer == null || developer.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        return gameRepository.findByDeveloper(developer.trim(), pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> getGamesByTag(String tag, Pageable pageable) {
        if (tag == null || tag.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        return gameRepository.findByTag(tag.trim(), pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> searchGamesByQuery(String query, Pageable pageable) {
        if (query == null || query.trim().isEmpty()) {
            return Page.empty(pageable);
        }
        return gameRepository.findByAllFields(query.trim().toLowerCase(), pageable)
                .map(GameMapper::toResponse);
    }

    @Transactional
    public void updateGameStats(Game game) {
        int added = gameEntryRepository.countByGame(game);
        Double rating = gameEntryRepository.averageRatingByGame(game);
        if (rating == null) rating = 0.0;
        gameRepository.updateStatsById(game.getId(), added, rating);
    }

   public Page<GameResponse> getComingSoonGames(Pageable pageable) {
        return gameRepository.findByReleasedAfterNowOrderByReleasedAsc(pageable).map(GameMapper::toResponse);
    }
}
