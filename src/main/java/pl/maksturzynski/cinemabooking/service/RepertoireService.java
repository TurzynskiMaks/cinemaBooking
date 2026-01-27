package pl.maksturzynski.cinemabooking.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.repository.ScreeningRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
