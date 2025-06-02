package it.MyGamingJournal.auth.User;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
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
    public List<AppUserResponse> getAllUsers() { return appUserRepository.findAll().stream().map(appUserService::fromEntity).toList(); }

    @GetMapping("/{id}")
    public AppUserResponse getUserById(@PathVariable Long id) {
        return appUserRepository.findById(id).map(appUserService::fromEntity).orElseThrow(() ->  new EntityNotFoundException("User not found"));
    }

    @GetMapping("/me")
    public AppUserResponse getCurrentUser(@AuthenticationPrincipal AppUser user) {
        return appUserService.fromEntity(user);
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


    @DeleteMapping("/me")
    public void deleteAccount(@AuthenticationPrincipal AppUser user) {
        appUserRepository.delete(user);
    }

    @PutMapping("/me/display-name")
    public void updateUserNames( @AuthenticationPrincipal AppUser user, @RequestBody AppUserNameRequest request) {
        appUserService.updateDisplayName(user.getId(), request);
    }

    @PutMapping("/me/bio")
    public void updateBio( @AuthenticationPrincipal AppUser user, @RequestBody AppUserBioRequest request) {
        appUserService.updateBio(user.getId(), request);
    }

    @PutMapping("/me/languages")
    public void updateLanguages(@AuthenticationPrincipal AppUser user,
                                @RequestBody AppUserLanguagesRequest request) {
        appUserService.updateLanguages(user.getId(), request);
    }

    @PutMapping("/me/contacts")
    public void updateContacts(@AuthenticationPrincipal AppUser user,
                               @RequestBody AppUserContactsRequest request) {
        appUserService.updateContacts(user.getId(), request);
    }

    @PutMapping("/me/follow/{userId}")
    public void toggleFollowUser(@AuthenticationPrincipal AppUser user, @PathVariable Long userId) {
        appUserService.toggleFollow(user, userId);
    }

    @GetMapping("/me/friends")
    public Page<FriendResponse> getMyFriends(
            @AuthenticationPrincipal AppUser user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return appUserService.getFriends(user, page, size);
    }
}
