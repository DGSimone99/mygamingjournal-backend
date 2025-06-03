package it.MyGamingJournal.gameEntry.gameEntry;


import it.MyGamingJournal.gameEntry.enums.GameStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GameEntryRequest {
    private Double hoursPlayed;

    @Min(value = 0, message = "Personal rating must be at least 0")
    @Max(value = 10, message = "Personal rating cannot exceed 10")
    private Double personalRating;

    private GameStatus status;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}