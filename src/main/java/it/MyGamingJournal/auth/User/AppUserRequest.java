package it.MyGamingJournal.auth.User;

import lombok.Data;

import java.util.List;

@Data
public class AppUserRequest {
    private String username;
    private String displayName;
    private String bio;
}