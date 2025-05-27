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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

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
            List<String> langs = registerRequest.getLanguages().stream()
                    .distinct()
                    .limit(3)
                    .toList();
            appUser.setLanguage(langs);
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

    public static AppUserResponse fromEntity(AppUser user) {
        AppUserResponse appUserResponse = new AppUserResponse();
        appUserResponse.id = user.getId();
        appUserResponse.username = user.getUsername();
        appUserResponse.email = user.getEmail();
        appUserResponse.displayName = user.getUsername();
        appUserResponse.avatarUrl = user.getAvatarUrl();
        appUserResponse.bio = user.getBio();
        appUserResponse.language = user.getLanguage();
        appUserResponse.createdAt = user.getCreatedAt();
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

        return new AppUserStatsResponse(totalGames, totalHours, completedCount, wishlistedCount, unlockedAchievements);
    }

}
