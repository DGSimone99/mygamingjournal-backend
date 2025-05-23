package it.MyGamingJournal.gameEntry.achievementEntry;


import it.MyGamingJournal.gameEntry.gameEntry.GameEntry;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementEntryRepository extends JpaRepository<AchievementEntry, Long> {
    List<AchievementEntry> findByGameEntry(GameEntry gameEntry, Sort sort);
}