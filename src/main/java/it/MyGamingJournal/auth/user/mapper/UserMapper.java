package it.MyGamingJournal.auth.user.mapper;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.userResponses.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserSettingsProfileResponse toUserSettingsProfileResponse(User user) {
        return UserSettingsProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .languages(List.copyOf(user.getLanguages()))
                .steamUsername(user.getSteamUsername())
                .psnUsername(user.getPsnUsername())
                .xboxUsername(user.getXboxUsername())
                .nintendoUsername(user.getNintendoUsername())
                .epicUsername(user.getEpicUsername())
                .riotId(user.getRiotId())
                .discordTag(user.getDiscordTag())
                .build();
    }

    public UserBasicInfoResponse toUserBasicInfoResponse(User user) {
        return UserBasicInfoResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .level(user.getLevel())
                .languages(List.copyOf(user.getLanguages()))
                .build();
    }

    public UserMinimalResponse toUserMinimalResponse(User user) {
        return UserMinimalResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

    public UserFullProfileResponse toUserFullProfileResponse(User user, UserStatsResponse userStatsResponse) {
        return UserFullProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .languages(List.copyOf(user.getLanguages()))
                .createdAt(user.getCreatedAt())

                .steamUsername(user.getSteamUsername())
                .psnUsername(user.getPsnUsername())
                .xboxUsername(user.getXboxUsername())
                .nintendoUsername(user.getNintendoUsername())
                .epicUsername(user.getEpicUsername())
                .riotId(user.getRiotId())
                .discordTag(user.getDiscordTag())

                .totalGames(userStatsResponse.getTotalGames())
                .totalHoursPlayed(userStatsResponse.getTotalHoursPlayed())
                .completedGamesCount(userStatsResponse.getCompletedGamesCount())
                .wishlistedGamesCount(userStatsResponse.getWishlistedGamesCount())
                .unlockedAchievements(userStatsResponse.getUnlockedAchievements())
                .level(userStatsResponse.getLevel())
                .experience(userStatsResponse.getExperience())
                .build();
    }
}

