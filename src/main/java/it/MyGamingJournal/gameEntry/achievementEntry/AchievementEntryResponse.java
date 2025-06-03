package it.MyGamingJournal.gameEntry.achievementEntry;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementEntryResponse {
    private Long id;
    private Long achievementId;
    private String name;
    private String description;
    private String image;
    private double averagePercentage;
    private boolean unlocked;
}
