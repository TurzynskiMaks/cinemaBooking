package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.SeatReservation;
import pl.maksturzynski.cinemabooking.domain.entity.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    List<SeatReservation> findByScreeningId(Long screeningId);

    long deleteByScreeningIdAndStatusAndHeldUntilBefore(Long screeningId, SeatStatus status, LocalDateTime time);

    boolean existsByScreeningIdAndSeatId(Long screeningId, Long seatId);
}
