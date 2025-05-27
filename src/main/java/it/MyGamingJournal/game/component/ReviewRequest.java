package it.MyGamingJournal.game.component;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRequest {
    @Column(columnDefinition = "TEXT")
    private String text;
    private int score;
}
