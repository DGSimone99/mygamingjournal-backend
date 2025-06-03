package it.MyGamingJournal.auth.user.userRequests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordRequest {
    @NotBlank(message = "Old password cannot be blank.")
    private String oldPassword;

    @NotBlank(message = "New password cannot be blank.")
    @Size(min = 8, message = "New password must be at least 8 characters long.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "New password must contain at least one uppercase letter and one number.")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be blank.")
    private String confirmNewPassword;
}
