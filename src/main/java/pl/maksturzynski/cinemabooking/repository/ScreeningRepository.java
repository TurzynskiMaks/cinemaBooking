package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findByStartTimeBetween (LocalDateTime from, LocalDateTime to);
    List<Screening> findByHallIdAndStartTimeBetween(Long hallId, LocalDateTime from, LocalDateTime to);
    boolean existsByFilmId(Long filmId);
    boolean existsByFilmIdAndStartTimeGreaterThanEqual(Long filmId, LocalDateTime from);
    List<Screening> findAllByFilmId(Long filmId);
    void deleteAllByFilmId(Long filmId);
}
