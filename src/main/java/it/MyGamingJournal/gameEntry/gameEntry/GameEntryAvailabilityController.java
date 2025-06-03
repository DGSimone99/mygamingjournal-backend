package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@RestController
@RequestMapping("/api/availability")
public class GameEntryAvailabilityController {
    @Autowired
    private GameEntryService gameEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameEntryRepository gameEntryRepository;



    @GetMapping("/{gameId}/available-players")
    public Page<GameEntryAvailabilityResponse> getAvailablePlayers(
            @PathVariable long gameId,
            @RequestParam(required = false) Set<String> languages,
            @RequestParam(required = false) Set<String> platforms,
            @PageableDefault(size = 10, sort = "availableUntil", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<GameEntryAvailabilityResponse> result = gameEntryService.getAvailablePlayers(
                gameId,
                languages,
                platforms,
                pageable
        );

        return result;
    }

    @PutMapping("/{gameEntryId}")
    public void updateAvailability(
            @PathVariable Long gameEntryId,
            @RequestBody @Valid GameEntryAvailabilityUpdateRequest request) {

        gameEntryService.updateAvailability(gameEntryId, request);
    }


}
