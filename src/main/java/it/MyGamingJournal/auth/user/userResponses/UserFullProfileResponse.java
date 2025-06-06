package it.MyGamingJournal.auth.user.userResponses;

import it.MyGamingJournal.gameEntry.gameEntry.GameEntryResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class UserFullProfileResponse {
    private Long id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private String bio;
    private List<String> languages;
    private LocalDateTime createdAt;

    private String steamUsername;
    private String psnUsername;
    private String xboxUsername;
    private String nintendoUsername;
    private String epicUsername;
    private String riotId;
    private String discordTag;

    private int totalGames;
    private double totalHoursPlayed;
    private long completedGamesCount;
    private long wishlistedGamesCount;
    private long unlockedAchievements;
    private int level;
    private int experience;

    private GameEntryResponse lastAddedGame;
    private GameEntryResponse mostPlayedGame;
}

