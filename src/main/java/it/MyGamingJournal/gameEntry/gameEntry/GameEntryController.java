package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserService;
import it.MyGamingJournal.gameEntry.enums.CompletionMode;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/my-library")
public class GameEntryController {
    @Autowired
    private GameEntryService gameEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameEntryRepository gameEntryRepository;

    @GetMapping
    public List<GameEntry> getGameEntries(@AuthenticationPrincipal User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return gameEntryService.getGamesByUser(user);
    }

    @GetMapping("/ids")
    public List <GameEntryResponse> getGameEntryResponse(@AuthenticationPrincipal User user) {
        return gameEntryService.getGameEntryResponse(user);
    }

    @PostMapping
    public GameEntry addGameEntry(@AuthenticationPrincipal User user,
                                  @RequestParam long idGame,
                                  @RequestParam(required = false) Double hoursPlayed,
                                  @RequestParam(required = false) Double personalRating,
                                  @RequestParam(required = false) GameStatus status,
                                  @RequestParam(required = false) CompletionMode completionMode,
                                  @RequestParam(required = false) String notes) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return gameEntryService.addGameEntry(
                user,
                idGame,
                hoursPlayed != null ? hoursPlayed : 0,
                personalRating != null ? personalRating : 0.0,
                status != null ? status : GameStatus.PLAYING,
                completionMode != null ? completionMode : CompletionMode.UNCOMPLETED,
                notes);
    }

    @PutMapping
    public GameEntry editGameEntry(@AuthenticationPrincipal User user,
                                  @RequestParam long idGame,
                                  @RequestParam(required = false) Double hoursPlayed,
                                  @RequestParam(required = false) Double personalRating,
                                  @RequestParam(required = false) GameStatus status,
                                  @RequestParam(required = false) CompletionMode completionMode,
                                  @RequestParam(required = false) String notes) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return gameEntryService.updateGameEntry(
                user,
                idGame,
                hoursPlayed != null ? hoursPlayed : 0,
                personalRating != null ? personalRating : 0.0,
                status != null ? status : GameStatus.PLAYING,
                completionMode != null ? completionMode : CompletionMode.UNCOMPLETED,
                notes);
    }

    @DeleteMapping
    public void deleteGameEntry(@AuthenticationPrincipal User user, @RequestParam long id) {
        gameEntryService.deleteGameEntry(user, id);
    }

    @GetMapping("{id}")
    public List<GameEntry> getUserGameEntries(@PathVariable long id) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return gameEntryService.getGamesByUser(user);
    }
}