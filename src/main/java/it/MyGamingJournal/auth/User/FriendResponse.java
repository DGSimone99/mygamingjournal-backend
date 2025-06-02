package it.MyGamingJournal.auth.User;

import lombok.Data;

import java.util.List;

@Data
public class FriendResponse {
    private Long id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private int level;
    private List<String> languages;
}