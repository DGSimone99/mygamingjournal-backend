package it.MyGamingJournal.rawg.relatedGames;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.rawg.RawgDetailsData;
import it.MyGamingJournal.rawg.RawgGameDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawgRelatedGamesResponse {
    private List<RelatedGame> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RelatedGame {
        private Long id;
        private String name;
        private String slug;
        private LocalDate released;
        @JsonProperty("background_image")
        private String backgroundImage;

        private List<Platforms> platforms;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Platforms {
            private Platform platform;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Platform {
                private int id;
                private String name;
                private String slug;
            }
        }
    }
}

