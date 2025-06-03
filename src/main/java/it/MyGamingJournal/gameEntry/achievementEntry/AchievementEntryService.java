package it.MyGamingJournal.gameEntry.achievementEntry;

import it.MyGamingJournal.auth.user.User;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import it.MyGamingJournal.gameEntry.gameEntry.GameEntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AchievementEntryService {

    private final AchievementEntryRepository achievementEntryRepository;
    private final GameEntryRepository gameEntryRepository;

    public List<AchievementEntry> getAchievementEntries(Long gameEntryId, String sortBy, String order) {
        GameEntry gameEntry = gameEntryRepository.findById(gameEntryId)
                .orElseThrow(() -> new EntityNotFoundException("GameEntry not found with id " + gameEntryId));

        Sort.Direction direction = Sort.Direction.ASC;
        if ("desc".equalsIgnoreCase(order)) {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(direction, sortBy);

        return achievementEntryRepository.findByGameEntry(gameEntry, sort);
    }

    public AchievementEntry updateAchievementEntry(Long id, AchievementEntryRequest request, User user) {
        AchievementEntry achievementEntry = achievementEntryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AchievementEntry not found with id " + id));

        if (!achievementEntry.getGameEntry().getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized to update this achievement entry");
        }

        achievementEntry.setUnlocked(request.isUnlocked());
        return achievementEntryRepository.save(achievementEntry);
    }
}

