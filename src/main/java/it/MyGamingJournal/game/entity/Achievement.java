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

    @Column(length = 100)
    private String name;
    @Column(length = 200)
    private String description;
    @Column(length = 512)
    private String image;

    @Column(name = "average_percentage")
    private double averagePercentage;

    @ManyToOne
    @JoinColumn(name="game_id")
    @JsonBackReference
    private Game game;
}