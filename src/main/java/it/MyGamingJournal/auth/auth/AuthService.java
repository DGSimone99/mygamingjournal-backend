package it.MyGamingJournal.auth.auth;

import it.MyGamingJournal.auth.JwtTokenUtil;
import it.MyGamingJournal.auth.user.Role;
import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.auth.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthResponse registerUser(@Valid RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new EntityExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new EntityExistsException("Email already exists");
        }

        User user = new User();

        if (registerRequest.getLanguages() != null) {
            List<String> langs = new ArrayList<>(
                    registerRequest.getLanguages().stream()
                            .distinct()
                            .limit(3)
                            .toList()
            );
            user.setLanguages(langs);
        }

        user.setUsername(registerRequest.getUsername());
        user.setDisplayName(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));

        userRepository.save(user);

        UserDetails userDetails = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new NoSuchElementException("User not found after registration"));

        String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthResponse(token);
    }

    public AuthResponse authenticateUser(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);

            return new AuthResponse(token);
        } catch (AuthenticationException e) {
            throw new SecurityException("Invalid credentials", e);
        }
    }
}
