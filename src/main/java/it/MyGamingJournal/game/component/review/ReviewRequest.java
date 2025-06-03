package it.MyGamingJournal.game.component.review;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRequest {
    @NotBlank(message = "Review text cannot be blank")
    private String text;

    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 10, message = "Score cannot be more than 10")
    private int score;
}
