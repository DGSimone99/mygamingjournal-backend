package it.MyGamingJournal.game.component.game;

import it.MyGamingJournal.game.entity.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/games")
public class GameController {

    final GameService gameService;

    @GetMapping
    public Page<GameResponse> getGames(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String developers,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "-released") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "18") int size) {

        Sort sort = Sort.unsorted();
        if (order != null && !order.isBlank()) {
            String[] fields = order.split(",");
            List<Sort.Order> orders = new ArrayList<>();

            for (String field : fields) {
                field = field.trim();
                if (!field.isEmpty()) {
                    Sort.Direction dir = field.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
                    String property = field.replace("-", "");
                    orders.add(new Sort.Order(dir, property));
                }
            }

            if (!orders.isEmpty()) {
                sort = Sort.by(orders);
            }
        }

        Pageable pageable = PageRequest.of(page, size, sort);

        if ("coming".equals(type)) {
            return gameService.getComingSoonGames(pageable);
        }

        if (genre != null && !genre.isEmpty()) {
            genre = genre.replace("-", " ").toLowerCase();
            return gameService.getGamesByGenre(genre, pageable);
        }

        if (name != null && !name.isEmpty()) {
            return gameService.searchGamesByName(name, pageable);
        }

        if (tags != null && !tags.isEmpty()) {
            return gameService.getGamesByTag(tags, pageable);
        }

        if (developers != null && !developers.isEmpty()) {
            return gameService.getGamesByDeveloper(developers, pageable);
        }

        if (query != null && !query.isEmpty()) {
            return gameService.searchGamesByQuery(query, pageable);
        }

        return gameService.getGames(pageable);
    }



    @GetMapping("/details/{id}")
    public Game getGame(@PathVariable Long id) {
        return gameService.getDetailsGame(id);
    }
}
