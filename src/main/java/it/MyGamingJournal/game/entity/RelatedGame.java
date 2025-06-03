package it.MyGamingJournal.game.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatedGame {
    private Long id;
    private String name;
    private String slug;
    private String backgroundImage;
    private LocalDate released;
    private Double rating;
    private List<String> parentPlatforms = new ArrayList<>();
}
