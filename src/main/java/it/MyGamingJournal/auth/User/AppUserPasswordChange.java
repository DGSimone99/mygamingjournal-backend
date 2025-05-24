package it.MyGamingJournal.auth.User;

import lombok.Data;

@Data
public class AppUserPasswordChange {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
