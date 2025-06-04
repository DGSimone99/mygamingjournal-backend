package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntryResponse;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import it.MyGamingJournal.gameEntry.enums.GameStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
public class GameEntryResponse {
    private Long id;

    private Long gameId;
    private String gameName;
    private String gameSlug;
    private String backgroundImage;

    private Double hoursPlayed;
    private Double personalRating;
    private GameStatus status;

    private String notes;

    private List<AchievementEntryResponse> achievements;

    private boolean availableToPlay;
    private List<String> availableLanguages;
    private Set<String> availablePlatforms;
    private LocalDate availableUntil;
}
