package it.MyGamingJournal.auth.User;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AppUserResponse {
   public Long id;
   public String username;
   public String displayName;
   public String email;
   public String avatarUrl;
   public String bio;
   public List<String> language;
   public LocalDate createdAt;
   public Boolean isOnline;

   public int totalGames;
   public double totalHoursPlayed;
   public long completedGamesCount;
   public long wishlistedGamesCount;
   public long unlockedAchievements;

}
