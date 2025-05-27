package it.MyGamingJournal.auth.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserStatsResponse {
    public int totalGames;
    public double totalHoursPlayed;
    public long completedGamesCount;
    public long wishlistedGamesCount;
    public long unlockedAchievements;
}
