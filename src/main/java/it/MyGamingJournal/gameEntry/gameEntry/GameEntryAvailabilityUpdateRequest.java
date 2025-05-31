package it.MyGamingJournal.gameEntry.gameEntry;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class GameEntryAvailabilityUpdateRequest {

    private boolean availableToPlay;

    @Size(max = 3, message = "At most 3 platforms are allowed.")
    private Set<String> availablePlatforms;
}
