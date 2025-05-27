package it.MyGamingJournal.game.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "games")

public class Game {
    @Id
    private Long id;

    @Column(length = 100)
    private String name;
    @Column(length = 100, unique = true)
    private String slug;

    @Column(name = "background_image", length = 512)
    private String backgroundImage;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String minimumRequirements;
    @Column(columnDefinition = "TEXT")
    private String recommendedRequirements;

    @Column(length = 512)
    private String redditUrl;
    @Column(length = 30)
    private String esrbRating;


    private LocalDate released;
    private Integer metacritic;
    private int added;
    private int achievementsCount;
    private double rating;


    @ElementCollection
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_platforms", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "platform")
    private List<String> parentPlatforms = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "platforms", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "platforms")
    private List<String> platforms = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_developers", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "developer")
    private List<String> developers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_publishers", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "publishers")
    private List<String> publishers = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "short_screenshots", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "screenshots")
    private List<String> screenshots = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tags", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "tags")
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name="game_modes", joinColumns = @JoinColumn(name = "game_id"))
    private List<String> gameModes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "parent_games", joinColumns = @JoinColumn(name = "game_id"))
    private List<ParentGame> parentGames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "related_games", joinColumns = @JoinColumn(name = "game_id"))
    private List<RelatedGame> relatedGames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "dlc_list", joinColumns = @JoinColumn(name = "game_id"))
    private List<Dlc> dlcList = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Achievement> achievements = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DeveloperMember> developmentTeam = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();


    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedGame {
        private Long id;
        private String name;
        private String slug;
        private LocalDate released;
        private String backgroundImage;

        private List<String> platforms = new ArrayList<>();
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dlc {
        private Long id;
        private String name;
        private String slug;
        private LocalDate released;
        private String backgroundImage;

        private List<String> platforms = new ArrayList<>();
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParentGame {
        private Long id;
        private String name;
        private String slug;
        private LocalDate released;
        private String backgroundImage;

        private List<String> platforms = new ArrayList<>();
    }
}