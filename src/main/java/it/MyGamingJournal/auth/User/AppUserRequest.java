package it.MyGamingJournal.auth.User;

import lombok.Data;

import java.util.List;

@Data
public class AppUserRequest {
    private String displayName;
    private String avatarUrl;
    private String bio;
    private List<String> language;
}
