package it.MyGamingJournal.auth;

import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.auth.User.AppUserService;
import it.MyGamingJournal.auth.User.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<AppUser> adminUser = appUserService.findByUsername("AdminSimone");
        if (adminUser.isEmpty()) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setUsername("AdminSimone");
            registerRequest.setEmail("simone.digiorgio99@gmail.com");
            registerRequest.setPassword("Adminpwd99");
            registerRequest.setLanguages(List.of("it", "en"));
            appUserService.registerUser(registerRequest);
        }
    }
}
