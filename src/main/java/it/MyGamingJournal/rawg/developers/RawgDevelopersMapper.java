package it.MyGamingJournal.rawg.developers;

import it.MyGamingJournal.game.entity.DeveloperMember;


import java.util.List;
import java.util.stream.Collectors;

public class RawgDevelopersMapper {
    public static List<DeveloperMember> toEntity(RawgDevelopersResponse rawgDeveloper) {
        return rawgDeveloper.getResults().stream().map(result -> {
            DeveloperMember developerMember = new DeveloperMember();
            developerMember.setId(result.getId());
            developerMember.setName(result.getName());
            developerMember.setSlug(result.getSlug());
            developerMember.setImage(result.getImage());

            if (result.getPositions() != null) {
                List<String> positionNames = result.getPositions().stream()
                        .map(RawgDevelopersResponse.RawgDeveloper.Position::getName)
                        .collect(Collectors.toList());
                developerMember.setPositions(positionNames);
            }
            return developerMember;
        }).collect(Collectors.toList());
    }
}
