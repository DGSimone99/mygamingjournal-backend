package it.MyGamingJournal.gameEntry.gameEntry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEntryResponse {
    private Long gameId;
}
