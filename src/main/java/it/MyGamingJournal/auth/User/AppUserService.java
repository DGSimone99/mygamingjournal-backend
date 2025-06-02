package it.MyGamingJournal.auth.User;

import it.MyGamingJournal.auth.JwtTokenUtil;
import it.MyGamingJournal.auth.RegisterRequest;
import it.MyGamingJournal.gameEntry.achievementEntry.AchievementEntry;
import it.MyGamingJournal.gameEntry.enums.GameStatus;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntryRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GameEntryRepository gameEntryRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void registerUser(RegisterRequest registerRequest) {
        if (appUserRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EntityExistsException("Username already exists");
        }

        if (appUserRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EntityExistsException("Email already exists");
        }


        AppUser appUser = new AppUser();

        if (registerRequest.getLanguages() != null) {
            List<String> langs = new ArrayList<>(
                    registerRequest.getLanguages().stream()
                            .distinct()
                            .limit(3)
                            .toList()
            );
            appUser.setLanguages(langs);
        }

        appUser.setUsername(registerRequest.getUsername());
        appUser.setDisplayName(registerRequest.getUsername());
        appUser.setEmail(registerRequest.getEmail());
        appUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        appUser.setRoles(Set.of(Role.ROLE_USER));
        appUserRepository.save(appUser);
    }

    public String authenticateUser(String username, String password)  {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            throw new SecurityException("Invalid credentials", e);
        }
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public AppUserResponse fromEntity(AppUser user) {
        AppUserResponse appUserResponse = new AppUserResponse();
        appUserResponse.id = user.getId();
        appUserResponse.username = user.getUsername();
        appUserResponse.email = user.getEmail();
        appUserResponse.displayName = user.getDisplayName();
        appUserResponse.avatarUrl = user.getAvatarUrl();
        appUserResponse.bio = user.getBio();
        appUserResponse.language = user.getLanguages();
        appUserResponse.createdAt = user.getCreatedAt();
        AppUserStatsResponse stats = getUserStats(user.getId());
        appUserResponse.level = stats.getLevel();
        appUserResponse.steamUsername = user.getSteamUsername();
        appUserResponse.psnUsername = user.getPsnUsername();
        appUserResponse.xboxUsername = user.getXboxUsername();
        appUserResponse.nintendoUsername = user.getNintendoUsername();
        appUserResponse.epicUsername = user.getEpicUsername();
        appUserResponse.riotId = user.getRiotId();
        appUserResponse.discordTag = user.getDiscordTag();

        return appUserResponse;
    }

    public AppUserStatsResponse getUserStats(Long userId) {
        AppUser user = appUserRepository.findByIdWithGameEntries(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<GameEntry> entries = gameEntryRepository.findAllWithAchievements(user.getGameEntries());

        int totalGames = entries.size();
        double totalHours = entries.stream()
                .mapToDouble(GameEntry::getHoursPlayed)
                .sum();
        long completedCount = entries.stream()
                .filter(e -> e.getStatus() == GameStatus.COMPLETED)
                .count();
        long wishlistedCount = entries.stream()
                .filter(e -> e.getStatus() == GameStatus.WISHLIST)
                .count();
        long unlockedAchievements = entries.stream()
                .flatMap(e -> e.getAchievements().stream())
                .filter(AchievementEntry::isUnlocked)
                .count();

        int totalExp = LevelingSystem.calculateExp(totalGames, (int) completedCount, (int) unlockedAchievements, totalHours);
        int level = LevelingSystem.calculateLevel(totalExp);


        return new AppUserStatsResponse(totalGames, totalHours, completedCount, wishlistedCount, unlockedAchievements, level, totalExp);
    }

    public class LevelingSystem {

        private static int expPerLevel = 500;

        public static int calculateExp(int gameEntries, int completedGames, int achievements, double hoursPlayed) {
            return (gameEntries * 50)
                    + (completedGames * 150)
                    + (achievements * 5)
                    + ((int) hoursPlayed * 5);
        }

        public static int calculateLevel(int totalExp) {
            return totalExp / expPerLevel;
        }
    }

    public void updateDisplayName(Long userId, AppUserNameRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setDisplayName(request.getDisplayName());
        appUserRepository.save(user);
    }

    public void updateBio(Long userId, AppUserBioRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setBio(request.getBio());
        appUserRepository.save(user);
    }

    public void updateLanguages(Long userId, AppUserLanguagesRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<String> langs = new ArrayList<>(
                request.getLanguages().stream()
                        .distinct()
                        .limit(3)
                        .toList()
        );
        user.setLanguages(langs);
        appUserRepository.save(user);
    }

    public void updateContacts(Long userId, AppUserContactsRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setSteamUsername(request.getSteamUsername());
        user.setPsnUsername(request.getPsnUsername());
        user.setXboxUsername(request.getXboxUsername());
        user.setNintendoUsername(request.getNintendoUsername());
        user.setEpicUsername(request.getEpicUsername());
        user.setRiotId(request.getRiotId());
        user.setDiscordTag(request.getDiscordTag());

        appUserRepository.save(user);
    }

    public void toggleFollow(AppUser follower, Long targetUserId) {
        AppUser target = appUserRepository.findById(targetUserId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        AppUser followerWithFriends = appUserRepository.findByIdWithFriends(follower.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Follower not found"));

        if (followerWithFriends.getFriends().contains(target)) {
            followerWithFriends.getFriends().remove(target);
        } else {
            followerWithFriends.getFriends().add(target);
        }

        appUserRepository.save(followerWithFriends);
    }

    public Page<FriendResponse> getFriends(AppUser user, int page, int size) {
        AppUser userWithFriends = appUserRepository.findByIdWithFriends(user.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<FriendResponse> allFriends = userWithFriends.getFriends().stream().map(friend -> {
            FriendResponse friendResponse = new FriendResponse();
            friendResponse.setId(friend.getId());
            friendResponse.setUsername(friend.getUsername());
            friendResponse.setDisplayName(friend.getDisplayName());
            friendResponse.setAvatarUrl(friend.getAvatarUrl());
            AppUserStatsResponse stats = getUserStats(user.getId());
            friendResponse.setLevel(stats.getLevel());
            friendResponse.setLanguages(friend.getLanguages());
            return friendResponse;
        }).toList();

        int start = Math.min(page * size, allFriends.size());
        int end = Math.min(start + size, allFriends.size());

        List<FriendResponse> paginatedList = allFriends.subList(start, end);
        return new PageImpl<>(paginatedList, PageRequest.of(page, size), allFriends.size());
    }




}