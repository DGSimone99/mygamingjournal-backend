package it.MyGamingJournal.auth.user.userRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserNameRequest {
    @NotBlank(message = "Display name cannot be blank.")
    @Size(max = 30, message = "Display name must be at most 30 characters.")
    private String displayName;
}