package it.MyGamingJournal.rawg.developers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawgDevelopersResponse {
   private List<RawgDeveloper> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RawgDeveloper {
        private Long id;
        private String name;
        private String slug;
        private String image;

        private List<Position> positions;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Position {
            private Long id;
            private String name;
            private String slug;
        }
    }
}
