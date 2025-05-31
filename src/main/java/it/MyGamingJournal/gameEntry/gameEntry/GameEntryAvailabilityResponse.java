package it.MyGamingJournal.gameEntry.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntryAvailabilityResponse {

    private String gameName;
    private String gameBackgroundImage;

    private String userDisplayName;
    private String userAvatarUrl;

    private Set<String> availablePlatforms;
    private Set<String> availableLanguages;
}
