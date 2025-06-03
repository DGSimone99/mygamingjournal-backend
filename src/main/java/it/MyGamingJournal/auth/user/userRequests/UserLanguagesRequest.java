package it.MyGamingJournal.auth.user.userRequests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserLanguagesRequest {
    @NotNull(message = "Languages list cannot be null.")
    @Size(min = 1, max = 3, message = "Languages must be between 1 and 3")
    private List<String> languages;
}
