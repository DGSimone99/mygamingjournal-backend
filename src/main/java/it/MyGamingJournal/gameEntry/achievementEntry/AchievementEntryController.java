package it.MyGamingJournal.gameEntry.achievementEntry;

import it.MyGamingJournal.auth.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/achievement-entries")
public class AchievementEntryController {

    private final AchievementEntryService achievementEntryService;

    @PatchMapping("/{id}")
    public ResponseEntity<AchievementEntryResponse> updateUnlockedStatus(
            @PathVariable Long id,
            @RequestBody AchievementEntryRequest request,
            @AuthenticationPrincipal User user) {

        AchievementEntry updatedEntry = achievementEntryService.updateAchievementEntry(id, request, user);

        AchievementEntryResponse response = AchievementEntryMapper.toResponse(updatedEntry);

        return ResponseEntity.ok(response);
    }
}

