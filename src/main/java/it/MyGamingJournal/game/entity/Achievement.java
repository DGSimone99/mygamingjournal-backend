package it.MyGamingJournal.game.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "achievements")
public class Achievement {
    @Id
    private Long id;

    @Column(length = 255, nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(length = 512)
    private String image;

    @Column(name = "average_percentage", nullable = false)
    private double averagePercentage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @JsonBackReference
    private Game game;
}
