package it.MyGamingJournal.game.component;

import it.MyGamingJournal.exceptions.GameNotFoundException;
import it.MyGamingJournal.game.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepository;

    public Game getDetailsGame(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }

    public Page<GameResponse> getGames(Pageable pageable) {
        return gameRepository.findAll(pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> searchGamesByName(String name, Pageable pageable) {
        return gameRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> getGamesByGenre(String genre, Pageable pageable) {
        return gameRepository.findByGenre(genre, pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> getGamesByDeveloper(String developer, Pageable pageable) {
        return gameRepository.findByDeveloper(developer, pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> getGamesByTag(String tag, Pageable pageable) {
        return gameRepository.findByTag(tag, pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> searchGamesByQuery(String query, Pageable pageable) {
        return gameRepository.findByAllFields(query.toLowerCase(), pageable)
                .map(GameMapper::toResponse);
    }

    public Page<GameResponse> searchGames(String name, Integer year, String platform, Double minRating, Pageable pageable) {
        return gameRepository.searchGames(name, year, platform, minRating, pageable)
                .map(GameMapper::toResponse);
    }

    @Transactional
    public void updateGame(Game game) {
        gameRepository.save(game);
    }

    @Transactional
    public void updateGameStats(Game game) {
        gameRepository.updateStatsById(game.getId(), game.getAdded(), game.getAverageRating());
    }
}
