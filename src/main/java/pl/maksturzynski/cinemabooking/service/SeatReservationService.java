package pl.maksturzynski.cinemabooking.service;


import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.domain.entity.Seat;
import pl.maksturzynski.cinemabooking.domain.entity.SeatReservation;
import pl.maksturzynski.cinemabooking.domain.entity.SeatStatus;
import pl.maksturzynski.cinemabooking.dto.api.SeatStatusResponse;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.repository.ScreeningRepository;
import pl.maksturzynski.cinemabooking.repository.SeatRepository;
import pl.maksturzynski.cinemabooking.repository.SeatReservationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeatReservationService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final SeatReservationRepository seatReservationRepository;

    public SeatReservationService(ScreeningRepository screeningRepository,
                                  SeatRepository seatRepository,
                                  SeatReservationRepository seatReservationRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.seatReservationRepository = seatReservationRepository;
    }

    public List<SeatStatusResponse> getSeatStatuses(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new BusinessException("Screening not found: " + screeningId));

        Long hallId = screening.getHall().getId();
        List<Seat> seats = seatRepository.findByHallIdOrderByRowNumAscSeatNumAsc(hallId);

        Map<Long, SeatReservation> bySeatId = new HashMap<>();
        for (SeatReservation sr : seatReservationRepository.findByScreeningId(screeningId)) {
            bySeatId.put(sr.getSeat().getId(), sr);
        }

        LocalDateTime now = LocalDateTime.now();

        List<SeatStatusResponse> out = new ArrayList<>();
        for (Seat seat : seats) {
            SeatStatusResponse r = new SeatStatusResponse();
            r.setSeatId(seat.getId());
            r.setRowNum(seat.getRowNum());
            r.setSeatNum(seat.getSeatNum());

            SeatReservation sr = bySeatId.get(seat.getId());
            if (sr == null) {
                r.setStatus("FREE");
            } else if (sr.getStatus() == SeatStatus.SOLD) {
                r.setStatus("SOLD");
            } else {
                r.setStatus("HELD");
            }

            out.add(r);
        }
        return out;
    }

    @Transactional
    public void holdSeats(Long screeningId, List<Long> seatIds, String sessionId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new BusinessException("Screening not found: " + screeningId));

        LocalDateTime now = LocalDateTime.now();

        seatReservationRepository.deleteByScreeningIdAndStatusAndHeldUntilBefore(
                screeningId, SeatStatus.HELD, now
        );

        LocalDateTime heldUntil = now.plusMinutes(10);

        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new BusinessException("Seat not found: " + seatId));

            SeatReservation sr = new SeatReservation();
            sr.setScreening(screening);
            sr.setSeat(seat);
            sr.setStatus(SeatStatus.HELD);
            sr.setHeldUntil(heldUntil);
            sr.setHolderSessionId(sessionId);

            try {
                seatReservationRepository.save(sr);
            } catch (DataIntegrityViolationException ex) {
                //UNIQUE = Somebody took that seat before
                throw new BusinessException("Seat already taken: " + seatId);
            }
        }
    }
}
