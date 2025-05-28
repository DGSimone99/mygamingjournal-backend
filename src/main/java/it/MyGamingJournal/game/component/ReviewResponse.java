package it.MyGamingJournal.game.component;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private String text;
    private int score;
    private LocalDate date;
    private Author author;
    private Game game;

    @Data
    @AllArgsConstructor
    public static class Author {
        private Long id;
        private String username;
        private String avatarUrl;
        private String displayName;
    }

    @Data
    @AllArgsConstructor
    public static class Game {
        private Long id;
        private String name;
        private String backgroundImage;
    }
}

