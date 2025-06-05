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
        registerRequest.setEmail("simone@example.com");
        registerRequest.setPassword("Simone99");
        registerRequest.setLanguages(List.of("it", "en"));
        authService.registerUser(registerRequest);

        RegisterRequest user1 = new RegisterRequest();
        user1.setUsername("Luna");
        user1.setEmail("luna@example.com");
        user1.setPassword("Luna99");
        user1.setLanguages(List.of("en", "es"));
        authService.registerUser(user1);

        RegisterRequest user2 = new RegisterRequest();
        user2.setUsername("Gustave");
        user2.setEmail("gustave@example.com");
        user2.setPassword("Gustave99");
        user2.setLanguages(List.of("it", "fr"));
        authService.registerUser(user2);

        RegisterRequest user3 = new RegisterRequest();
        user3.setUsername("David");
        user3.setEmail("david@example.com");
        user3.setPassword("David99");
        user3.setLanguages(List.of("de", "en"));
        authService.registerUser(user3);

        RegisterRequest user4 = new RegisterRequest();
        user4.setUsername("Jack");
        user4.setEmail("jack@example.com");
        user4.setPassword("Jack99");
        user4.setLanguages(List.of("en"));
        authService.registerUser(user4);

        RegisterRequest user5 = new RegisterRequest();
        user5.setUsername("Ripley");
        user5.setEmail("ripley@example.com");
        user5.setPassword("Ripley99");
        user5.setLanguages(List.of("en", "jp"));
        authService.registerUser(user5);
    }
}
*/
