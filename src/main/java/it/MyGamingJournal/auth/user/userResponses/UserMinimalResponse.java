package it.MyGamingJournal.auth.user.userResponses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserMinimalResponse {
    private Long id;
    private String username;
    private String displayName;
    private String avatarUrl;
}