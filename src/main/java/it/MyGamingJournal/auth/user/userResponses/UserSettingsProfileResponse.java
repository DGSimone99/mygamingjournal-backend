package it.MyGamingJournal.auth.user.userResponses;

import lombok.Data;
import lombok.Builder;

import java.util.List;

@Builder
@Data
public class UserSettingsProfileResponse {
   private Long id;
   private String username;
   private String displayName;
   private String email;
   private String avatarUrl;
   private String bio;
   private List<String> languages;

   private String steamUsername;
   private String psnUsername;
   private String xboxUsername;
   private String nintendoUsername;
   private String epicUsername;
   private String riotId;
   private String discordTag;
}