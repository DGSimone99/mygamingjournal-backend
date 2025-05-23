package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.auth.User.AppUser;
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
    private GameEntryRepository gameEntryRepository;

    @GetMapping
    public List<GameEntry> getGameEntries(@AuthenticationPrincipal AppUser user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }
        return gameEntryService.getGamesByUser(user);
    }

    @PostMapping
    public GameEntry addGameEntry(@AuthenticationPrincipal AppUser user,
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
                idGame,
                user,
                hoursPlayed != null ? hoursPlayed : 0,
                personalRating != null ? personalRating : 0.0,
                status != null ? status : GameStatus.PLAYING,
                completionMode != null ? completionMode : CompletionMode.UNCOMPLETED,
                notes);
    }

    @PutMapping
    public GameEntry editGameEntry(@AuthenticationPrincipal AppUser user,
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
                idGame,
                user,
                hoursPlayed != null ? hoursPlayed : 0,
                personalRating != null ? personalRating : 0.0,
                status != null ? status : GameStatus.PLAYING,
                completionMode != null ? completionMode : CompletionMode.UNCOMPLETED,
                notes);
    }

    @DeleteMapping
    public void deleteGameEntry(@AuthenticationPrincipal AppUser user, @RequestParam long idGame) {
        gameEntryService.deleteGameEntry(idGame, user);
    }
}