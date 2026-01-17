package pl.maksturzynski.cinemabooking.service;


import org.springframework.stereotype.Service;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.repository.ScreeningRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepertoireService {

    private final ScreeningRepository screeningRepository;

    public RepertoireService(ScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    public List<Screening> getScreeningsForDate(LocalDate date) {
        LocalDateTime from = date.atStartOfDay();
        LocalDateTime to = date.plusDays(1).atStartOfDay();
        return screeningRepository.findByStartTimeBetween(from, to);
    }
}
