package it.MyGamingJournal.rawg;


import it.MyGamingJournal.game.entity.Achievement;
import it.MyGamingJournal.game.entity.Game;
import it.MyGamingJournal.game.entity.DeveloperMember;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RawgGameMapper {
    public static Game toEntity(RawgGameDto dto, RawgDetailsData details, List<Game.RelatedGames> relatedGames, List<Game.Dlc> dlc, List<Achievement> achievements, List<DeveloperMember> developmentTeam) {
        Game game = new Game();

        game.setId(dto.getId());
        game.setName(dto.getName());
        game.setSlug(dto.getSlug());
        game.setBackgroundImage(dto.getBackgroundImage());

        try {
            game.setReleased(LocalDate.parse(dto.getReleased()));
        } catch (Exception e) {
            game.setReleased(null);
        }


        game.setRating(dto.getMetacritic());

        List<String> genres = dto.getGenres().stream()
                .map(RawgGameDto.Genre::getName)
                .collect(Collectors.toList());
        game.setGenres(genres);

        List<String> platforms = dto.getParentPlatforms().stream()
                .map(platform -> platform.getPlatform().getName())
                .collect(Collectors.toList());
        game.setParentPlatforms(platforms);

        List<String> screenshots = dto.getScreenshots().stream().map(RawgGameDto.ShortScreenshot::getImage).collect(Collectors.toList());
        game.setScreenshots(screenshots);

        List<String> allTags = dto.getTags().stream()
                .map(RawgGameDto.Tags::getName)
                .filter(tag -> !tag.matches(".*\\p{IsCyrillic}.*"))
                .collect(Collectors.toList());

        List<String> modes = dto.getTags().stream()
                .map(RawgGameDto.Tags::getName)
                .filter(tag -> tag.equalsIgnoreCase("Singleplayer") || tag.equalsIgnoreCase("Multiplayer"))
                .collect(Collectors.toList());

        List<String> tags = allTags.stream()
                .filter(tag -> !modes.contains(tag))
                .toList();

        game.setGameModes(modes);
        game.setTags(tags);

        if (dto.getEsrbRating() != null) {
            game.setEsrbRating(dto.getEsrbRating().getName());
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
        game.setDlcList(dlc);

        return game;
    }
}
