
package it.MyGamingJournal.runner;

import it.MyGamingJournal.rawg.RawgApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameBootstrap implements CommandLineRunner {

    private final RawgApiService rawgApiService;

    @Override
    public void run(String... args) {
        rawgApiService.fetchAndSaveGames(1);
    }
}
