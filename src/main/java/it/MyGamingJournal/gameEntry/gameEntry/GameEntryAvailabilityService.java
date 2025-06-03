package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.game.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
public class GameEntryAvailabilityService {

    private final GameEntryRepository gameEntryRepository;

    public Page<GameEntryAvailabilityResponse> getAvailablePlayers(
            long realGameId,
            Set<String> languages,
            Set<String> platforms,
            Pageable pageable) {

        return gameEntryRepository.findAvailablePlayers(realGameId, languages, platforms, pageable)
                .map(this::toAvailabilityDTO);
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

    @Transactional
    public void updateAvailability(Long gameEntryId, GameEntryAvailabilityUpdateRequest request) {
        GameEntry entry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GameEntry not found"));

        entry.setAvailableToPlay(request.isAvailableToPlay());

        if (request.isAvailableToPlay()) {
            Set<String> requestedPlatforms = request.getAvailablePlatforms();
            if (requestedPlatforms == null || requestedPlatforms.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one platform must be selected");
            }
            if (requestedPlatforms.size() > 3) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum 3 platforms allowed");
            }

            Set<String> validPlatforms = new HashSet<>(entry.getGame().getPlatforms());
            if (!validPlatforms.containsAll(requestedPlatforms)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Selected platforms are invalid");
            }

            entry.setAvailablePlatforms(requestedPlatforms);
            entry.setAvailableUntil(LocalDate.now().plusDays(14));

            User user = entry.getUser();
            if (user == null || user.getLanguages() == null || user.getLanguages().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User languages not found");
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
    @Transactional
    public void expireGameEntryAvailability() {
        List<GameEntry> expiredEntries = gameEntryRepository.findByAvailableToPlayTrueAndAvailableUntilBefore(LocalDate.now());
        expiredEntries.forEach(entry -> {
            entry.setAvailableToPlay(false);
            entry.setAvailableLanguages(Collections.emptyList());
            entry.setAvailableUntil(null);
        });
        gameEntryRepository.saveAll(expiredEntries);
    }
}

