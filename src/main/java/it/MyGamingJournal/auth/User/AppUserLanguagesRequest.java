package it.MyGamingJournal.auth.User;

import lombok.Data;

import java.util.List;

@Data
public class AppUserLanguagesRequest {
    private List<String> languages;
}
