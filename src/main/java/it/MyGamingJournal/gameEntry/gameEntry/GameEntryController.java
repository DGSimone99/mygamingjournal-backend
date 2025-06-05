package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserService;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/my-library")
@PreAuthorize("isAuthenticated()")
public class GameEntryController {

    @Autowired
    private GameEntryService gameEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<GameEntryResponse> getGameEntries(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return gameEntryService.getGameEntryResponse(user);
    }

    @PostMapping
    public GameEntryResponse addGameEntry(
            @AuthenticationPrincipal User user,
            @RequestParam long idGame,
            @RequestBody @Valid GameEntryRequest gameEntryRequest) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        GameEntry savedEntry = gameEntryService.addGameEntry(
                user,
                idGame,
                gameEntryRequest
        );
        return GameEntryMapper.toResponse(savedEntry);
    }

    @PutMapping("/{gameId}")
    public GameEntryResponse editGameEntry(
            @AuthenticationPrincipal User user,
            @PathVariable long gameId,
            @RequestBody @Valid GameEntryRequest request) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        GameEntry updatedEntry = gameEntryService.updateGameEntry(user, gameId, request);
        return GameEntryMapper.toResponse(updatedEntry);
    }

    @DeleteMapping("/{gameEntryId}")
    public void deleteGameEntry(
            @AuthenticationPrincipal User user,
            @PathVariable long gameEntryId) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        gameEntryService.deleteGameEntry(user, gameEntryId);
    }

    @GetMapping("/user/{userId}")
    public List<GameEntryResponse> getUserGameEntries(@PathVariable long userId) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return gameEntryService.getGameEntryResponse(user);
    }

    @GetMapping("/ids")
    public ResponseEntity<List<Long>> getUserGameEntryIds(@AuthenticationPrincipal User user) {
        List<Long> ids = gameEntryService.getGameEntryIdsByUser(user);
        return ResponseEntity.ok(ids);
    }
}
