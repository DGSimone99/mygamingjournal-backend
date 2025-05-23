package it.MyGamingJournal.rawg;

import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.component.GameRepository;
import it.MyGamingJournal.game.entity.DeveloperMember;
import it.MyGamingJournal.rawg.achievements.RawgAchievementMapper;
import it.MyGamingJournal.rawg.achievements.RawgAchievementsResponse;
import it.MyGamingJournal.rawg.developers.RawgDevelopersMapper;
import it.MyGamingJournal.rawg.developers.RawgDevelopersResponse;
import it.MyGamingJournal.rawg.relatedGames.RawgRelatedGamesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawgApiService {
    private final GameRepository gameRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${rawg.api.key}")
    private String rawgApiKey;

    public void fetchAndSaveGames(int totalPages) {
        for (int page = 1; page <= totalPages; page++) {
            String url = "https://api.rawg.io/api/games?page=" + page +
                    "&page_size=1" +
                    "&ordering=-added" +
                    "&dates=2020-05-29,2025-05-29&key=" + rawgApiKey;


            RawgGameListResponse rawgGameListReponse = restTemplate.getForObject(url, RawgGameListResponse.class);

            if (rawgGameListReponse != null && rawgGameListReponse.getResults() != null) {
                List<Game> gamesToSave = rawgGameListReponse.getResults().stream()
                        .filter(game -> game.getBackgroundImage() != null && game.getRating() >= 2 && game.getParentPlatforms() != null)
                        .map(dto -> {
                            RawgDetailsData data = fetchDevelopersById(dto.getId());
                            List<Achievement> achievements = fetchAllAchievementsForGame(dto.getId());
                            List<DeveloperMember> developmentTeam = fetchDeveloperTeamById(dto.getId());
                            List<Game.RelatedGames> relatedGames = fetchRelatedById(dto.getId())
                                    .stream()
                                    .map(relatedGame -> new Game.RelatedGames(
                                            relatedGame.getId(),
                                            relatedGame.getName(),
                                            relatedGame.getSlug(),
                                            relatedGame.getReleased(),
                                            relatedGame.getBackgroundImage(),
                                            relatedGame.getPlatforms().stream()
                                                    .map(pw -> pw.getPlatform().getName())  // oppure getSlug() o getId()
                                                    .collect(Collectors.toList())
                                    ))
                                    .collect(Collectors.toList());
                            List<Game.Dlc> dlc = fetchDlcById(dto.getId())
                                    .stream()
                                    .map(dlcGame -> new Game.Dlc(
                                            dlcGame.getId(),
                                            dlcGame.getName(),
                                            dlcGame.getSlug(),
                                            dlcGame.getReleased(),
                                            dlcGame.getBackgroundImage(),
                                            dlcGame.getPlatforms().stream()
                                                    .map(pw -> pw.getPlatform().getName()) // o .getSlug() / .getId()
                                                    .collect(Collectors.toList())
                                    ))
                                    .collect(Collectors.toList());
                            return RawgGameMapper.toEntity(dto, data, relatedGames, dlc, achievements, developmentTeam);
                        })
                        .collect(Collectors.toList());

                gameRepository.saveAll(gamesToSave);
                System.out.println("Pagina " + page + ": salvati " + gamesToSave.size() + " giochi.");
            }
        }
    }

    private RawgDetailsData fetchDevelopersById(Long gameId) {
        String url = "https://api.rawg.io/api/games/" + gameId + "?key=" + rawgApiKey;
        RawgDetailsData rawgDetailsData = restTemplate.getForObject(url, RawgDetailsData.class);
        if(rawgDetailsData != null) {
            return rawgDetailsData;
        }
        return null;
    }

    public List<Achievement> fetchAllAchievementsForGame(Long gameId) {
        RestTemplate restTemplate = new RestTemplate();
        int page = 1;

        List<Achievement> allAchievements = new ArrayList<>();
        Map<Long, Achievement> achievementMap = new HashMap<>();

        while (true) {
            String url = "https://api.rawg.io/api/games/" + gameId + "/achievements?key=" + rawgApiKey + "&page=" + page;

            RawgAchievementsResponse response = restTemplate.getForObject(url, RawgAchievementsResponse.class);

            if (response == null || response.getResults() == null || response.getResults().isEmpty()) {
                break;
            }

            List<Achievement> achievements = RawgAchievementMapper.toEntity(response);

            for (Achievement a : achievements) {
                achievementMap.putIfAbsent(a.getId(), a);
            }

            if (response.getNext() == null) {
                break;
            }

            page++;
        }

        return new ArrayList<>(achievementMap.values());
    }

    private List<RawgRelatedGamesResponse.RelatedGame> fetchRelatedById(Long gameId) {
        String url = "https://api.rawg.io/api/games/" + gameId + "/game-series?key=" + rawgApiKey;
        RawgRelatedGamesResponse relatedGame = restTemplate.getForObject(url, RawgRelatedGamesResponse.class);

        if(relatedGame != null) {
            return relatedGame.getResults();
        }
        return List.of();
    }

    private List<RawgRelatedGamesResponse.RelatedGame> fetchDlcById(Long gameId) {
        String url = "https://api.rawg.io/api/games/" + gameId + "/additions?key=" + rawgApiKey;
        RawgRelatedGamesResponse dlc = restTemplate.getForObject(url, RawgRelatedGamesResponse.class);

        if(dlc != null) {
            return dlc.getResults();
        }
        return List.of();
    }

    private List<DeveloperMember> fetchDeveloperTeamById(Long gameId) {
        String url = "https://api.rawg.io/api/games/" + gameId + "/development-team?key=" + rawgApiKey;
        RawgDevelopersResponse rawgDevelopersResponse = restTemplate.getForObject(url, RawgDevelopersResponse.class);

        if(rawgDevelopersResponse != null) {
            List<DeveloperMember> developers = RawgDevelopersMapper.toEntity(rawgDevelopersResponse);
            return developers;
        }
        return List.of();
    }
}
