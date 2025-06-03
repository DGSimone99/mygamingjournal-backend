package it.MyGamingJournal.auth.user;

import org.springframework.stereotype.Component;

@Component
public class LevelingSystem {

    private static final int BASE_EXP = 1000;

    private static final int WEIGHT_TOTAL_GAMES = 40;
    private static final int WEIGHT_COMPLETED_GAMES = 120;
    private static final int WEIGHT_UNLOCKED_ACHIEVEMENTS = 15;
    private static final int WEIGHT_HOURS_PLAYED = 5;

    public int calculateExp(int totalGames, int completedGames, int unlockedAchievements, double hoursPlayed) {
        int hours = (int) Math.floor(hoursPlayed);

        int expGames = totalGames * WEIGHT_TOTAL_GAMES;
        int expCompleted = completedGames * WEIGHT_COMPLETED_GAMES;
        int expAchievements = unlockedAchievements * WEIGHT_UNLOCKED_ACHIEVEMENTS;
        int expHours = hours * WEIGHT_HOURS_PLAYED;

        return expGames + expCompleted + expAchievements + expHours;
    }

    public int calculateLevel(int totalExp) {
        return totalExp / BASE_EXP;
    }
}
