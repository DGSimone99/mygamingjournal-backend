package it.MyGamingJournal.auth.user;

import it.MyGamingJournal.auth.user.mapper.UserMapper;
import it.MyGamingJournal.auth.user.userRequests.UserBioRequest;
import it.MyGamingJournal.auth.user.userRequests.UserContactsRequest;
import it.MyGamingJournal.auth.user.userRequests.UserLanguagesRequest;
import it.MyGamingJournal.auth.user.userRequests.UserNameRequest;
import it.MyGamingJournal.auth.user.userResponses.*;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GameEntryRepository gameEntryRepository;
    private final UserMapper userMapper;
    private final LevelingSystem levelingSystem;

    public User findUserById(Long userId) {
        return userRepository.findByIdWithGameEntries(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }


    public Page<UserBasicInfoResponse> getAllUsers(int page, int size, String query) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;

        if (query == null || query.trim().isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.searchByUsernameOrDisplayName(query, pageable);
        }

        return users.map(userMapper::toUserBasicInfoResponse);
    }

    public UserFullProfileResponse getFullProfile(Long userId) {
        User user = findUserById(userId);
        UserStatsResponse stats = getUserStats(user.getId());

        return userMapper.toUserFullProfileResponse(user, stats);
    }

    public UserSettingsProfileResponse getSettingsProfile(Long userId) {
        User user = findUserById(userId);
        return userMapper.toUserSettingsProfileResponse(user);
    }

    public UserMinimalResponse getMinimalProfile(Long userId) {
        User user = findUserById(userId);
        return userMapper.toUserMinimalResponse(user);
    }

    public Page<UserBasicInfoResponse> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toUserBasicInfoResponse);
    }

    public UserStatsResponse getUserStats(Long userId) {
        User user = findUserById(userId);

        List<GameEntry> gameEntries = gameEntryRepository.findAllWithAchievements(user.getGameEntries());

        int totalGames = gameEntries.size();
        double totalHours = gameEntries.stream()
                .mapToDouble(GameEntry::getHoursPlayed)
                .sum();

        long completedCount = gameEntries.stream()
                .filter(e -> e.getStatus() == GameStatus.COMPLETED)
                .count();

        long wishlistedCount = gameEntries.stream()
                .filter(e -> e.getStatus() == GameStatus.WISHLIST)
                .count();

        long unlockedAchievements = gameEntries.stream()
                .flatMap(e -> e.getAchievements().stream())
                .filter(AchievementEntry::isUnlocked)
                .count();

        int totalExp = levelingSystem.calculateExp(totalGames, (int) completedCount, (int) unlockedAchievements, totalHours);
        int level = levelingSystem.calculateLevel(totalExp);

        return new UserStatsResponse(
                totalGames,
                Math.round(totalHours * 10.0) / 10.0,
                completedCount,
                wishlistedCount,
                unlockedAchievements,
                level,
                totalExp
        );
    }

    //UPDATES
    public void updateDisplayName(Long userId, UserNameRequest request) {
        User user = findUserById(userId);

        user.setDisplayName(request.getDisplayName());
        userRepository.save(user);
    }

    public void updateBio(Long userId, UserBioRequest request) {
        User user = findUserById(userId);

        user.setBio(request.getBio());
        userRepository.save(user);
    }

    public void updateLanguages(Long userId, UserLanguagesRequest request) {
        User user = findUserById(userId);

        List<String> langs = new ArrayList<>(
                request.getLanguages().stream()
                        .distinct()
                        .limit(3)
                        .toList()
        );
        user.setLanguages(langs);
        userRepository.save(user);
    }

    public void updateContacts(Long userId, UserContactsRequest request) {
        User user = findUserById(userId);

        user.setSteamUsername(request.getSteamUsername());
        user.setPsnUsername(request.getPsnUsername());
        user.setXboxUsername(request.getXboxUsername());
        user.setNintendoUsername(request.getNintendoUsername());
        user.setEpicUsername(request.getEpicUsername());
        user.setRiotId(request.getRiotId());
        user.setDiscordTag(request.getDiscordTag());

        userRepository.save(user);
    }


    //FRIENDS
    public User findUserByIdWithFriends(Long userId) {
        return userRepository.findByIdWithFriends(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }

    public void toggleFollow(User follower, Long targetUserId) {
        User targetUser = findUserById(targetUserId);

        User followerWithFriends = findUserByIdWithFriends(follower.getId());

        if (followerWithFriends.getFriends().contains(targetUser)) {
            followerWithFriends.getFriends().remove(targetUser);
            userRepository.save(followerWithFriends);
        } else {
            followerWithFriends.getFriends().add(targetUser);
            userRepository.save(followerWithFriends);
        }
    }

    public Page<UserBasicInfoResponse> getFriends(Long userId, int page, int size, String query) {
        Pageable pageable = PageRequest.of(page, size);
        String search = query == null ? "" : query.toLowerCase();

        Page<User> friendsPage = userRepository.findFriendsByUserId(userId, search, pageable);

        return friendsPage.map(userMapper::toUserBasicInfoResponse);
    }
}
