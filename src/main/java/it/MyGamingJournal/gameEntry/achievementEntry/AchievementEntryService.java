package it.MyGamingJournal.gameEntry.achievementEntry;

import it.MyGamingJournal.auth.User.AppUser;
import it.MyGamingJournal.exceptions.GameNotFoundException;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AchievementEntryService {

    @Autowired
    private AchievementEntryRepository achievementEntryRepository;

    @Autowired
    private GameEntryRepository gameEntryRepository;

    public List<AchievementEntry> getAchievementEntries(Long gameEntryId, String sortBy, String order) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new RuntimeException("GameEntry not found"));

        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        return achievementEntryRepository.findByGameEntry(gameEntry, sort);
    }

    public AchievementEntry updateAchievementEntry(Long id, AchievementEntryRequest request, AppUser user) {
        AchievementEntry achievementEntry = achievementEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AchievementEntry not found"));

        achievementEntry.setUnlocked(request.isUnlocked());
        if (!achievementEntry.getGameEntry().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }

        return achievementEntryRepository.save(achievementEntry);
    }
}
