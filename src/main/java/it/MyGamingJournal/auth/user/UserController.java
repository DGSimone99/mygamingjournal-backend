package it.MyGamingJournal.auth.user;

import it.MyGamingJournal.auth.user.userRequests.*;
import it.MyGamingJournal.auth.user.userResponses.UserBasicInfoResponse;
import it.MyGamingJournal.auth.user.userResponses.UserFullProfileResponse;
import it.MyGamingJournal.auth.user.userResponses.UserSettingsProfileResponse;
import it.MyGamingJournal.auth.user.userResponses.UserMinimalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping
    public Page<UserBasicInfoResponse> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query
    ) {
        return userService.getAllUsers(page, size, query);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserFullProfileResponse> getMyFullProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getFullProfile(user.getId()));
    }

    @GetMapping("/me/settings")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserSettingsProfileResponse> getSettingsProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getSettingsProfile(user.getId()));
    }

    @GetMapping("/me/minimal")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserMinimalResponse> getMinimalProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getMinimalProfile(user.getId()));
    }


    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void changePassword(@AuthenticationPrincipal User user,
                               @Valid @RequestBody UserPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new password and the confirmed new password do not match.");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void deleteAccount(@AuthenticationPrincipal User user) {
        userService.deleteUser(user.getId());
    }

    @PutMapping("/me/display-name")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updateUserNames(@AuthenticationPrincipal User user, @Valid @RequestBody UserNameRequest request) {
        userService.updateDisplayName(user.getId(), request);
    }

    @PutMapping("/me/bio")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updateBio(@AuthenticationPrincipal User user, @Valid @RequestBody UserBioRequest request) {
        userService.updateBio(user.getId(), request);
    }

    @PutMapping("/me/languages")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updateLanguages(@AuthenticationPrincipal User user, @Valid @RequestBody UserLanguagesRequest request) {
        userService.updateLanguages(user.getId(), request);
    }

    @PutMapping("/me/contacts")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void updateContacts(@AuthenticationPrincipal User user, @Valid @RequestBody UserContactsRequest request) {
        userService.updateContacts(user.getId(), request);
    }

    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@AuthenticationPrincipal User user,
                                               @RequestPart("file") MultipartFile file) {
        String url = userService.uploadUserAvatar(user.getId(), file);
        return ResponseEntity.ok(url);
    }

    @PutMapping("/me/follow/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void toggleFollowUser(@AuthenticationPrincipal User user, @PathVariable Long userId) {
        if (user.getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself.");
        }
        userService.toggleFollow(user, userId);
    }

    @GetMapping("/me/friends")
    @PreAuthorize("isAuthenticated()")
    public Page<UserBasicInfoResponse> getFriends(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query) {
        return userService.getFriends(user.getId(), page, size, query);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserFullProfileResponse> getUserFullProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFullProfile(id));
    }
}
