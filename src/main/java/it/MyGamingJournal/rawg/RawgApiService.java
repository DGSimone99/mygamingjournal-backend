package it.MyGamingJournal.rawg;

import it.MyGamingJournal.game.component.game.GameResponse;
import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.component.game.GameRepository;
import it.MyGamingJournal.game.entity.DeveloperMember;
import it.MyGamingJournal.game.entity.RelatedGame;
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
            String url = "https://api.rawg.io/api/games?page=" + page + "&page_size=40&search=zelda&key=2e814586e02a41c783ed042617fd6dc3";

            try {
                RawgGameListResponse rawgGameListResponse = restTemplate.getForObject(url, RawgGameListResponse.class);

                if (rawgGameListResponse != null && rawgGameListResponse.getResults() != null) {
                    List<Game> gamesToSave = new ArrayList<>();


                    for (RawgGameDto dto : rawgGameListResponse.getResults()) {
                        /*String name = dto.getName().toLowerCase();
                        if (!name.contains("metal gear")) {
                            System.out.println("Scartato (non RE): " + dto.getName());
                            continue;
                        }*/
                       try {
                                RawgDetailsData data = fetchDevelopersById(dto.getId());
                                List<Achievement> achievements = fetchAllAchievementsForGame(dto.getId());
                                List<DeveloperMember> developmentTeam = fetchDeveloperTeamById(dto.getId());

                                List<RelatedGame> parentGames = fetchParentById(dto.getId()).stream()
                                        .map(parentGame -> RelatedGame.builder()
                                                .id(parentGame.getId())
                                                .name(parentGame.getName())
                                                .slug(parentGame.getSlug())
                                                .released(parentGame.getReleased())
                                                .backgroundImage(parentGame.getBackgroundImage())
                                                .parentPlatforms(parentGame.getParentPlatforms().stream()
                                                        .map(pw -> pw.getPlatform().getName())
                                                        .collect(Collectors.toList()))
                                                .build())
                                        .collect(Collectors.toList());

                                List<RelatedGame> relatedGames = fetchRelatedById(dto.getId()).stream()
                                        .map(related -> RelatedGame.builder()
                                                .id(related.getId())
                                                .name(related.getName())
                                                .slug(related.getSlug())
                                                .released(related.getReleased())
                                                .backgroundImage(related.getBackgroundImage())
                                                .parentPlatforms(related.getParentPlatforms().stream()
                                                        .map(pw -> pw.getPlatform().getName())
                                                        .collect(Collectors.toList()))
                                                .build())
                                        .collect(Collectors.toList());

                                List<RelatedGame> dlc = fetchDlcById(dto.getId()).stream()
                                        .map(dlcGame -> RelatedGame.builder()
                                                .id(dlcGame.getId())
                                                .name(dlcGame.getName())
                                                .slug(dlcGame.getSlug())
                                                .released(dlcGame.getReleased())
                                                .backgroundImage(dlcGame.getBackgroundImage())
                                                .parentPlatforms(dlcGame.getParentPlatforms().stream()
                                                        .map(pw -> pw.getPlatform().getName())
                                                        .collect(Collectors.toList()))
                                                .build())
                                        .collect(Collectors.toList());

                                Game gameEntity = RawgGameMapper.toEntity(dto, data, parentGames, relatedGames, dlc, achievements, developmentTeam);
                                gamesToSave.add(gameEntity);
                           System.out.println("Elaboro: " + dto.getName());
                                Thread.sleep(1000);

                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                System.err.println("Sleep interrotto tra giochi: " + e.getMessage());
                            } catch (Exception e) {
                                System.err.println("Errore durante il parsing del gioco con ID " + dto.getId() + ": " + e.getMessage());
                            }
                        }


                    gameRepository.saveAll(gamesToSave);
                    System.out.println("Pagina " + page + ": salvati " + gamesToSave.size() + " giochi.");
                }

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Sleep interrotto tra pagine: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println("Errore nella pagina " + page + ": " + ex.getMessage());
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

    private List<RawgRelatedGamesResponse.RelatedGame> fetchParentById(Long gameId) {
        String url = "https://api.rawg.io/api/games/" + gameId + "/parent-games?key=" + rawgApiKey;
        RawgRelatedGamesResponse parentGame = restTemplate.getForObject(url, RawgRelatedGamesResponse.class);

        if(parentGame != null) {
            return parentGame.getResults();
        }
        return List.of();
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
