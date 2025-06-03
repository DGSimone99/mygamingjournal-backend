package it.MyGamingJournal.gameEntry.gameEntry;

import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntryResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import it.MyGamingJournal.gameEntry.enums.GameStatus;

import java.util.List;

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
}
