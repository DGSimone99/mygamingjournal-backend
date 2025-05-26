package it.MyGamingJournal.rawg;


import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.DeveloperMember;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RawgGameMapper {
    public static Game toEntity(RawgGameDto rawgGameDto, RawgDetailsData details, List<Game.ParentGame> parentGames, List<Game.RelatedGame> relatedGames, List<Game.Dlc> dlc, List<Achievement> achievements, List<DeveloperMember> developmentTeam) {
        Game game = new Game();

        game.setId(rawgGameDto.getId());
        game.setName(rawgGameDto.getName());
        game.setSlug(rawgGameDto.getSlug());
        game.setBackgroundImage(rawgGameDto.getBackgroundImage());

        try {
            game.setReleased(LocalDate.parse(rawgGameDto.getReleased()));
        } catch (Exception e) {
            game.setReleased(null);
        }


        game.setMetacritic(rawgGameDto.getMetacritic());


        List<String> genres = rawgGameDto.getGenres().stream()
                .map(RawgGameDto.Genre::getName)
                .collect(Collectors.toList());
        game.setGenres(genres);

        List<String> platforms = rawgGameDto.getParentPlatforms().stream()
                .map(platform -> platform.getPlatform().getName())
                .collect(Collectors.toList());
        game.setParentPlatforms(platforms);

        List<String> screenshots = rawgGameDto.getScreenshots().stream().map(RawgGameDto.ShortScreenshot::getImage).collect(Collectors.toList());
        game.setScreenshots(screenshots);

        List<String> allTags = rawgGameDto.getTags().stream()
                .map(RawgGameDto.Tags::getName)
                .filter(tag -> !tag.matches(".*\\p{IsCyrillic}.*"))
                .collect(Collectors.toList());

        List<String> modes = rawgGameDto.getTags().stream()
                .map(RawgGameDto.Tags::getName)
                .filter(tag -> tag.equalsIgnoreCase("Singleplayer") || tag.equalsIgnoreCase("Multiplayer")  || tag.equalsIgnoreCase("Online multiplayer")  || tag.equalsIgnoreCase("Cross-Platform Multiplayer") )
                .collect(Collectors.toList());

        List<String> tags = allTags.stream()
                .toList();

        game.setGameModes(modes);
        game.setTags(tags);

        if (rawgGameDto.getEsrbRating() != null) {
            game.setEsrbRating(rawgGameDto.getEsrbRating().getName());
        } else {
            game.setEsrbRating("Not Rated");
        }


        if (details.getPlatforms() != null) {

            List<String> platformList = new ArrayList<>();
            for (RawgDetailsData.Platforms platformData : details.getPlatforms()) {
                if (platformData.getPlatform() != null) {
                    platformList.add(platformData.getPlatform().getName());
                    if (platformData.getPlatform() != null && platformData.getPlatform().getSlug().equals("pc")) {
                        if (platformData.getRequirements() != null) {
                            game.setMinimumRequirements(platformData.getRequirements().getMinimum());
                            game.setRecommendedRequirements(platformData.getRequirements().getRecommended());
                        } else {
                            game.setMinimumRequirements("Nessun requisito minimo consigliato");
                            game.setRecommendedRequirements("Nessun requisito raccomandato consigliato");
                        }
                    }
                }
                game.setPlatforms(platformList);
            }
        }

        if (achievements != null) {
            game.setAchievements(achievements);
            game.setAchievementsCount(achievements.size());
            for (Achievement a : achievements) {
                a.setGame(game);
            }
        }

        if (developmentTeam != null) {
            game.setDevelopmentTeam(developmentTeam);
            for (DeveloperMember m : developmentTeam) {
                m.setGame(game);
            }
        }

        game.setDevelopers(details.getDevelopers().stream().map(RawgDetailsData.Developer::getName).collect(Collectors.toList()));
        game.setPublishers(details.getPublishers().stream().map(RawgDetailsData.Publishers::getName).collect(Collectors.toList()));
        game.setRedditUrl(details.getRedditUrl());
        game.setDescription(details.getDescription_raw());
        game.setRelatedGames(relatedGames);
        game.setParentGames(parentGames);
        game.setDlcList(dlc);

        return game;
    }
}
