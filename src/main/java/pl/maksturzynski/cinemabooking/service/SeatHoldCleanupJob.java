package pl.maksturzynski.cinemabooking.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksturzynski.cinemabooking.domain.entity.SeatStatus;
import pl.maksturzynski.cinemabooking.repository.SeatReservationRepository;

import java.time.LocalDateTime;

@Service
public class SeatHoldCleanupJob {

    private final SeatReservationRepository repo;

    public SeatHoldCleanupJob(SeatReservationRepository repo) {
        this.repo = repo;
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void cleanupExpiredHolds() {
        repo.deleteByStatusAndHeldUntilBefore(SeatStatus.HELD, LocalDateTime.now());
    }
}
