package it.MyGamingJournal.gameEntry.achievementEntry;

import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;

public class AchievementEntryMapper {
    public static AchievementEntry fromAchievement(Achievement achievement, GameEntry gameEntry) {
        return AchievementEntry.builder()
                .achievementId(achievement.getId())
                .name(achievement.getName())
                .description(achievement.getDescription())
                .image(achievement.getImage())
                .averagePercentage(achievement.getAveragePercentage())
                .unlocked(false)
                .gameEntry(gameEntry)
                .build();
    }

    public static AchievementEntryResponse toResponse(AchievementEntry achievementEntry) {
        return AchievementEntryResponse.builder()
                .id(achievementEntry.getId())
                .achievementId(achievementEntry.getAchievementId())
                .name(achievementEntry.getName())
                .description(achievementEntry.getDescription())
                .image(achievementEntry.getImage())
                .averagePercentage(achievementEntry.getAveragePercentage())
                .unlocked(achievementEntry.isUnlocked())
                .build();
    }
}