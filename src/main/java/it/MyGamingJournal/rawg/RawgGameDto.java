package it.MyGamingJournal.rawg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawgGameDto {
    private Long id;
    private String name;
    private String slug;

    @JsonProperty("background_image")
    private String backgroundImage;

    private String released;
    private double rating;
    private int metacritic;

    private List<Genre> genres;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Genre {
        private String name;
    }

    @JsonProperty("parent_platforms")
    private List<ParentPlatform> parentPlatforms;

    @JsonProperty("platforms")
    private List<ParentPlatform> platforms;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ParentPlatform {
        private Platform platform;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Platform {
            private String name;
        }
    }

    @JsonProperty("short_screenshots")
    private List<ShortScreenshot> screenshots;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShortScreenshot {
        private String image;
    }

    private List<Tags> tags;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tags {
        private String name;
    }


    @JsonProperty("esrb_rating")
    private EsrbRating esrbRating;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EsrbRating {
        private String name;
    }
}
