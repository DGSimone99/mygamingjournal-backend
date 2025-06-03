package it.MyGamingJournal.game.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Game {
    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, unique = true, nullable = false)
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
    @Column(name = "genres")
    private List<String> genres = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "parent_platforms", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "parent_platform")
    private List<String> parentPlatforms = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_platforms", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "platforms")
    private List<String> platforms = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_developers", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "developers")
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
    @CollectionTable(name = "game_tags", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "tags")
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "game_modes", joinColumns = @JoinColumn(name = "game_id"))
    @Column(name = "game_modes")
    private List<String> gameModes = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Achievement> achievements = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeveloperMember> developmentTeam = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "related_games", joinColumns = @JoinColumn(name = "game_id"))
    private List<RelatedGame> relatedGames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "parent_games", joinColumns = @JoinColumn(name = "game_id"))
    private List<RelatedGame> parentGames = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "dlc_list", joinColumns = @JoinColumn(name = "game_id"))
    private List<RelatedGame> dlcList = new ArrayList<>();
}