package it.MyGamingJournal.auth.User;

import lombok.Data;

@Data
public class AppUserNameRequest {
    private String username;
    private String displayName;
}