package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/availability")
public class GameEntryAvailabilityController {
    private final GameEntryAvailabilityService gameEntryAvailabilityService;

    private final UserService userService;

    private final GameEntryRepository gameEntryRepository;

    @GetMapping("/{gameId}/available-players")
    public Page<GameEntryAvailabilityResponse> getAvailablePlayers(
            @PathVariable long gameId,
            @RequestParam(required = false) Set<String> languages,
            @RequestParam(required = false) Set<String> platforms,
            @PageableDefault(size = 10, sort = "availableUntil", direction = Sort.Direction.DESC) Pageable pageable) {

        return gameEntryAvailabilityService.getAvailablePlayers(
                gameId,
                languages,
                platforms,
                pageable
        );
    }

    @PutMapping("/{gameEntryId}")
    public void updateAvailability(
            @PathVariable Long gameEntryId,
            @RequestBody @Valid GameEntryAvailabilityUpdateRequest request) {

        gameEntryAvailabilityService.updateAvailability(gameEntryId, request);
    }
}
