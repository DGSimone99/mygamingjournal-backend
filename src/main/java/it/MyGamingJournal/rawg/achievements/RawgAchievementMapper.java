package it.MyGamingJournal.rawg.achievements;

import it.MyGamingJournal.game.entity.Achievement;
import java.util.List;
import java.util.stream.Collectors;

public class RawgAchievementMapper {
    public static List<Achievement> toEntity(RawgAchievementsResponse rawgAchievementsResponse) {
        return rawgAchievementsResponse.getResults().stream().map(result -> {
            Achievement achievement = new Achievement();
            achievement.setId(result.getId());
            achievement.setName(result.getName());
            achievement.setDescription(result.getDescription());
            achievement.setImage(result.getImage());
            achievement.setAveragePercentage(result.getPercent());
            return achievement;
        }).collect(Collectors.toList());
    }
}
