package it.MyGamingJournal.game.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class DeveloperMember {
    @Id
    private Long id;

    @Column(length = 100)
    private String name;
    @Column(length = 100)
    private String slug;
    @Column(length = 512)
    private String image;

    @ManyToOne
    @JoinColumn(name="game_id")
    @JsonBackReference
    private Game game;

    @ElementCollection
    @CollectionTable(name = "developer_positions", joinColumns = @JoinColumn(name = "developer_id"))
    @Column(name = "position")
    private List<String> positions = new ArrayList<>();
}