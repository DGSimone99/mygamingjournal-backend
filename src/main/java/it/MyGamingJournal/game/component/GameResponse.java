package it.MyGamingJournal.game.component;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class GameResponse {
    private Long id;
    private String name;
    private String slug;
    private String backgroundImage;
    private LocalDate released;
    private Double rating;
    private List<String> parentPlatforms;
    private List<String> developers;
}
