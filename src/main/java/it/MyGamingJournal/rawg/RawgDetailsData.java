package it.MyGamingJournal.rawg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.List;

@Data
public class RawgDetailsData {
    private List<Developer> developers;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Developer {
        private String name;
    }

    private List<Publishers> publishers;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Publishers {
        private String name;
    }

    @JsonProperty("reddit_url")
    private String redditUrl;

    @Column(columnDefinition = "TEXT")
    private String description_raw;

    private List<Platforms> platforms;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Platforms {
        private Platform platform;
        private Requirements requirements;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Platform {
            private int id;
            private String name;
            private String slug;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Requirements {
            private String minimum;
            private String recommended;
        }
    }
}
