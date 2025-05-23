package it.MyGamingJournal.gameEntry.achievementEntry;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "achievement_entries")

public class AchievementEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Long achievementId;
    @Column(length = 150)
    private String name;
    @Column(length = 250)
    private String description;
    @Column(length = 512)
    private String image;

    @ManyToOne
    @JoinColumn(name = "game_entry_id", nullable = false)
    @JsonBackReference
    private GameEntry gameEntry;

    @Column(name = "average_percentage")
    private double averagePercentage;

    private boolean unlocked = false;
}