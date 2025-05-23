package it.MyGamingJournal.gameEntry.achievementEntry;

import it.MyGamingJournal.auth.User.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/achievement-entries")
public class AchievementEntryController {

    @Autowired
    private AchievementEntryService achievementEntryService;

    @PatchMapping("/{id}")
    public AchievementEntry updateUnlockedStatus(
            @PathVariable Long id,
            @RequestBody AchievementEntryRequest request,
            @AuthenticationPrincipal AppUser user) {
        return achievementEntryService.updateAchievementEntry(id, request, user);
    }
}

