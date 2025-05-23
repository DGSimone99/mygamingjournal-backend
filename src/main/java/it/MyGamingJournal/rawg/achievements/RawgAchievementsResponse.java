package it.MyGamingJournal.rawg.achievements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawgAchievementsResponse {
    private String next;

    private List<Results> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Results {
        private Long id;
        private String name;
        private String description;
        private String image;
        private double percent;
    }
}
