package it.MyGamingJournal.gameEntry.achievementEntry;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "achievement_entries")
public class AchievementEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, name = "achievement_id")
    private Long achievementId;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 512)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_entry_id", nullable = false)
    @JsonBackReference
    private GameEntry gameEntry;

    @Column(name = "average_percentage")
    private double averagePercentage;

    private boolean unlocked = false;
}
