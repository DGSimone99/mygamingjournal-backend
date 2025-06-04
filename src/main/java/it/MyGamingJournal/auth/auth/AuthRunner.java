/*

package it.MyGamingJournal.auth.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthRunner implements ApplicationRunner {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("Simone");
        registerRequest.setEmail("simone.digiorgio99@gmail.com");
        registerRequest.setPassword("Adminpwd99");
        registerRequest.setLanguages(List.of("it", "en"));
        authService.registerUser(registerRequest);


        RegisterRequest user1 = new RegisterRequest();
        user1.setUsername("LunaPlayer");
        user1.setEmail("luna.player@example.com");
        user1.setPassword("TestPass1!");
        user1.setLanguages(List.of("en", "es"));
        authService.registerUser(user1);

        RegisterRequest user2 = new RegisterRequest();
        user2.setUsername("GameMaster92");
        user2.setEmail("gamemaster92@example.com");
        user2.setPassword("TestPass2!");
        user2.setLanguages(List.of("it", "fr"));
        authService.registerUser(user2);

        RegisterRequest user3 = new RegisterRequest();
        user3.setUsername("PixelNomad");
        user3.setEmail("pixel.nomad@example.com");
        user3.setPassword("TestPass3!");
        user3.setLanguages(List.of("de", "en"));
        authService.registerUser(user3);

        RegisterRequest user4 = new RegisterRequest();
        user4.setUsername("JoyStickJay");
        user4.setEmail("joystick.jay@example.com");
        user4.setPassword("TestPass4!");
        user4.setLanguages(List.of("en"));
        authService.registerUser(user4);

        RegisterRequest user5 = new RegisterRequest();
        user5.setUsername("ShadowFox");
        user5.setEmail("shadow.fox@example.com");
        user5.setPassword("TestPass5!");
        user5.setLanguages(List.of("en", "jp"));
        authService.registerUser(user5);
    }
}
*/
