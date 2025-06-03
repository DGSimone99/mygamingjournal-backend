package it.MyGamingJournal.gameEntry.gameEntry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.enums.CompletionMode;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Table(name = "game_entries")

public class GameEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "real_game_id")
    private Long realGameId;

    @Column(name = "game_name", length = 255)
    private String gameName;
    @Column(name = "game_slug", length = 255)
    private String gameSlug;

    @JsonIgnore
    @ManyToOne
    private Game game;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @Column(name = "hours_played")
    private Double hoursPlayed;
    @Column(name = "personal_rating")
    private Double personalRating;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "completion_mode")
    private CompletionMode completionMode;

    @Column(name= "background_image", length = 512)
    private String backgroundImage;

    @Column(name = "available_to_play")
    private boolean availableToPlay = false;

    @ElementCollection
    @CollectionTable(name = "game_entry_languages", joinColumns = @JoinColumn(name = "game_entry_id"))
    @Column(name = "languages")
    private List<String> availableLanguages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_entry_platforms", joinColumns = @JoinColumn(name = "game_entry_id"))
    @Column(name = "platforms")
    private Set<String> availablePlatforms = new HashSet<>();

    @Column(name = "available_until")
    private LocalDate availableUntil;


    @Column( columnDefinition = "TEXT")
    private String notes = "";


    @OneToMany(mappedBy = "gameEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AchievementEntry> achievements = new ArrayList<>();
}