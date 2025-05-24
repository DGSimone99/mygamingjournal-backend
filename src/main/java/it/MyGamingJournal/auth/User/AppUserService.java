package it.MyGamingJournal.auth.User;

import it.MyGamingJournal.auth.JwtTokenUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private JwtTokenUtil jwtTokenUtil;

    public void registerUser(String username, String email, String password, Set<Role> roles) {
        if (appUserRepository.existsByUsername(username)) {
            throw new EntityExistsException("Username already exists");
        }

        if (appUserRepository.existsByEmail(email)) {
            throw new EntityExistsException("Email already exists");
        }

        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setEmail(email);
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.setRoles(roles);
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

    public AppUser loadUserByUsername(String username)  {
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return appUser;
    }

    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    public static AppUserResponse fromEntity(AppUser user) {
        AppUserResponse appUserResponse = new AppUserResponse();
        appUserResponse.id = user.getId();
        appUserResponse.username = user.getUsername();
        appUserResponse.displayName = user.getDisplayName();
        appUserResponse.avatarUrl = user.getAvatarUrl();
        appUserResponse.bio = user.getBio();
        appUserResponse.language = user.getLanguage();
        appUserResponse.createdAt = user.getCreatedAt();
        appUserResponse.isOnline = user.getIsOnline();

        appUserResponse.totalGames = user.getTotalGames();
        appUserResponse.totalHoursPlayed = user.getTotalHoursPlayed();
        appUserResponse.completedGamesCount = user.getCompletedGamesCount();
        appUserResponse.wishlistedGamesCount = user.getWishlistedGamesCount();
        appUserResponse.unlockedAchievements = user.getUnlockedAchievements();

        return appUserResponse;
    }
}
