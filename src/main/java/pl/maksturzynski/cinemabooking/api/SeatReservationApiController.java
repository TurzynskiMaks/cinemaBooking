package pl.maksturzynski.cinemabooking.api;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.maksturzynski.cinemabooking.dto.api.HoldSeatRequest;
import pl.maksturzynski.cinemabooking.dto.api.SeatStatusResponse;
import pl.maksturzynski.cinemabooking.service.SeatReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/screenings")
public class SeatReservationApiController {

    private final SeatReservationService seatReservationService;

    public SeatReservationApiController(SeatReservationService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }

    @GetMapping("/{screeningId}/seats")
    public List<SeatStatusResponse> seats(@PathVariable Long screeningId) {
        return seatReservationService.getSeatStatuses(screeningId);
    }

    @PostMapping("/{screeningId}/hold")
    public ResponseEntity<Void> hold(
            @PathVariable Long screeningId,
            @Valid @RequestBody HoldSeatRequest request
            ) {
        seatReservationService.holdSeats(screeningId, request.getSeatIds());
        return ResponseEntity.noContent().build();
    }
}
