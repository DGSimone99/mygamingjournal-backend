package it.MyGamingJournal.auth.user.userRequests;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserBioRequest {
    @Size(max = 500, message = "Bio must be at most 500 characters.")
    private String bio;
}
