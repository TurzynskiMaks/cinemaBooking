package pl.maksturzynski.cinemabooking.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.dto.web.ScreeningForm;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.repository.FilmRepository;
import pl.maksturzynski.cinemabooking.repository.HallRepository;
import pl.maksturzynski.cinemabooking.repository.ScreeningRepository;

import java.time.LocalDateTime;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public ScreeningService(ScreeningRepository screeningRepository,
                            FilmRepository filmRepository,
                            HallRepository hallRepository) {
        this.screeningRepository = screeningRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    @Transactional
    public Screening createWithOverlapCheck(ScreeningForm form) {
        var film = filmRepository.findById(form.getFilmId())
                .orElseThrow(() -> new BusinessException("Film not found"));
        var hall = hallRepository.findById(form.getHallId())
                .orElseThrow(() -> new BusinessException("Hall not found"));

        LocalDateTime newStart = form.getStartTime();
        LocalDateTime newEnd = newStart.plusMinutes(form.getDurationMinutes());

        LocalDateTime dayStart = newStart.toLocalDate().atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);

        var existing = screeningRepository.findByHallIdAndStartTimeBetween(hall.getId(), dayStart, dayEnd);

        for (var s : existing) {
            LocalDateTime sStart = s.getStartTime();
            LocalDateTime sEnd = sStart.plusMinutes(s.getDurationMinutes());
            boolean overlap = sStart.isBefore(newEnd) && sEnd.isAfter(newStart);

            if(overlap) {
                throw new BusinessException("Overlap between new and old screening occured");
            }
        }

        Screening screening = new Screening();
        screening.setFilm(film);
        screening.setHall(hall);
        screening.setStartTime(newStart);
        screening.setDurationMinutes(form.getDurationMinutes());
        return screeningRepository.save(screening);
    }
}
