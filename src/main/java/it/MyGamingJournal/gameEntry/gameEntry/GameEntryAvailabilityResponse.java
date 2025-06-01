package it.MyGamingJournal.gameEntry.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntryAvailabilityResponse {
    private long gameEntryId;
    private String gameName;
    private String gameBackgroundImage;

    private String userDisplayName;
    private String userAvatarUrl;
    private long userId;

    private LocalDate availableUntil;
    private Set<String> availablePlatforms;
    private List<String> availableLanguages;
}
