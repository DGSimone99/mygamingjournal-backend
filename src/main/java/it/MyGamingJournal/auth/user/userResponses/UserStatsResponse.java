package it.MyGamingJournal.auth.user.userResponses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsResponse {
    private int totalGames;
    private double totalHoursPlayed;
    private long completedGamesCount;
    private long wishlistedGamesCount;
    private long unlockedAchievements;
    private int level;
    private int experience;
}
