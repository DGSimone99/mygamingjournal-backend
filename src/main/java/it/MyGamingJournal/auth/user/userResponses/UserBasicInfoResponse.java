package it.MyGamingJournal.auth.user.userResponses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserBasicInfoResponse {
    private Long id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private int level;
    private List<String> languages;
}