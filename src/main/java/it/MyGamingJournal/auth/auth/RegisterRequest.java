package it.MyGamingJournal.auth.auth;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RegisterRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d).{6,}$",
            message = "Password must be at least 6 characters, contain one uppercase letter and one number"
    )
    private String password;

    @NotNull(message = "At least one language is required")
    @Size(min = 1, max = 3, message = "Languages must be between 1 and 3")
    private List<@NotBlank(message = "Language cannot be blank") String> languages;
}