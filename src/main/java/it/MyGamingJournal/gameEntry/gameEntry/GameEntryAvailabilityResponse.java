package it.MyGamingJournal.gameEntry.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntryAvailabilityResponse {
    private Long gameEntryId;
    private String gameName;
    private String gameBackgroundImage;

    private String userDisplayName;
    private String userAvatarUrl;
    private Long userId;

    private LocalDate availableUntil;
    private Set<String> availablePlatforms = new HashSet<>();
    private List<String> availableLanguages = new ArrayList<>();
}
