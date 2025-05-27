package it.MyGamingJournal.game.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.MyGamingJournal.auth.User.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    private int score;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name="game_id")
    @JsonBackReference
    private Game game;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference
    private AppUser user;
}
