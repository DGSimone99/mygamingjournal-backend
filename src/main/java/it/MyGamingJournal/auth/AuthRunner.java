/*
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
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("Simone");
        registerRequest.setEmail("simone.digiorgio99@gmail.com");
        registerRequest.setPassword("Adminpwd99");
        registerRequest.setLanguages(List.of("it", "en"));
        appUserService.registerUser(registerRequest);


        RegisterRequest user1 = new RegisterRequest();
        user1.setUsername("LunaPlayer");
        user1.setEmail("luna.player@example.com");
        user1.setPassword("TestPass1!");
        user1.setLanguages(List.of("en", "es"));
        appUserService.registerUser(user1);

        RegisterRequest user2 = new RegisterRequest();
        user2.setUsername("GameMaster92");
        user2.setEmail("gamemaster92@example.com");
        user2.setPassword("TestPass2!");
        user2.setLanguages(List.of("it", "fr"));
        appUserService.registerUser(user2);

        RegisterRequest user3 = new RegisterRequest();
        user3.setUsername("PixelNomad");
        user3.setEmail("pixel.nomad@example.com");
        user3.setPassword("TestPass3!");
        user3.setLanguages(List.of("de", "en"));
        appUserService.registerUser(user3);

        RegisterRequest user4 = new RegisterRequest();
        user4.setUsername("JoyStickJay");
        user4.setEmail("joystick.jay@example.com");
        user4.setPassword("TestPass4!");
        user4.setLanguages(List.of("en"));
        appUserService.registerUser(user4);

        RegisterRequest user5 = new RegisterRequest();
        user5.setUsername("ShadowFox");
        user5.setEmail("shadow.fox@example.com");
        user5.setPassword("TestPass5!");
        user5.setLanguages(List.of("en", "jp"));
        appUserService.registerUser(user5);
    }
}
*/
