package it.MyGamingJournal.game.component.game;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResponse {
    private Long id;
    private String name;
    private String slug;
    private String backgroundImage;
    private LocalDate released;
    private Double rating;
    private Integer metacritic;
    private List<String> parentPlatforms = new ArrayList<>();
    private List<String> developers = new ArrayList<>();
}
