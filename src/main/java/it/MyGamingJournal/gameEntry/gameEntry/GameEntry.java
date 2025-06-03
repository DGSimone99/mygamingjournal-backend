package it.MyGamingJournal.gameEntry.gameEntry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_entries")
public class GameEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    @JsonIgnore
    private Game game;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "hours_played")
    private Double hoursPlayed;

    @Column(name = "personal_rating")
    private Double personalRating;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;


    @Column(name = "available_to_play")
    private boolean availableToPlay = false;

    @ElementCollection
    @CollectionTable(name = "game_entry_languages", joinColumns = @JoinColumn(name = "game_entry_id"))
    @Column(name = "language")
    private List<String> availableLanguages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_entry_platforms", joinColumns = @JoinColumn(name = "game_entry_id"))
    @Column(name = "platform")
    private Set<String> availablePlatforms = new HashSet<>();

    @Column(name = "available_until")
    private LocalDate availableUntil;

    @Column(columnDefinition = "TEXT")
    private String notes = "";

    @OneToMany(mappedBy = "gameEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AchievementEntry> achievements = new ArrayList<>();
}
