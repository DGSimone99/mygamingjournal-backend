package it.MyGamingJournal.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private List<String> languages;
}
