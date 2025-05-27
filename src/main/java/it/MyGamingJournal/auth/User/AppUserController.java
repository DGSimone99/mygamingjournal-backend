package it.MyGamingJournal.auth.User;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Data
@RestController
@RequestMapping("/api/users")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public List<AppUserResponse> getAllUsers() { return appUserRepository.findAll().stream().map(AppUserService::fromEntity).toList(); }

    @GetMapping("/{id}")
    public AppUserResponse getUserById(Long id) {
        return appUserRepository.findById(id).map(AppUserService::fromEntity).orElseThrow(() ->  new EntityNotFoundException("User not found"));
    }

    @GetMapping("/me")
    public AppUserResponse getCurrentUser(@AuthenticationPrincipal AppUser user) {
        return AppUserService.fromEntity(user);
    }

    @GetMapping("/me/stats")
    public AppUserStatsResponse getCurrentUserStats(@AuthenticationPrincipal AppUser user) {
        AppUserStatsResponse stats = appUserService.getUserStats(user.getId());
        return stats;
    }

    @GetMapping("/{id}/stats")
    public AppUserStatsResponse getUserStats(@PathVariable Long id) {
        AppUserStatsResponse stats = appUserService.getUserStats(id);
        return stats;
    }

    @PutMapping
    public void updateCurrentUserName(@AuthenticationPrincipal AppUser user,
                                  @RequestBody AppUserNameRequest appUserNameRequest) {
        String newUsername = appUserNameRequest.getUsername();

        if (!user.getUsername().equals(newUsername) &&
                appUserRepository.existsByUsername(newUsername)) {
            throw new EntityExistsException("Username already exists");
        }

        user.setUsername(appUserNameRequest.getUsername());
        user.setDisplayName(appUserNameRequest.getDisplayName());

        appUserRepository.save(user);
    }

    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal AppUser user,
                               @RequestBody AppUserPasswordChange appUserPasswordChange) {
        if (!appUserPasswordChange.getNewPassword().equals(appUserPasswordChange.getConfirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new password and the confirmed new password do not match.");
        }

        if (!passwordEncoder.matches(appUserPasswordChange.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(appUserPasswordChange.getNewPassword()));
        appUserRepository.save(user);
    }


    @DeleteMapping
    public void deleteAccount(@AuthenticationPrincipal AppUser user) {
        appUserRepository.delete(user);
    }

}
