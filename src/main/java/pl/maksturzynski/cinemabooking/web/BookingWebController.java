package pl.maksturzynski.cinemabooking.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.maksturzynski.cinemabooking.service.SeatReservationService;

@Controller
public class BookingWebController {

    private final SeatReservationService seatReservationService;

    public BookingWebController(SeatReservationService seatReservationService) {
        this.seatReservationService = seatReservationService;
    }

    @GetMapping("/booking/{screeningId}")
    public String booking(@PathVariable Long screeningId, Model model) {
        model.addAttribute("screeningId", screeningId);
        model.addAttribute("seats", seatReservationService.getSeatStatuses(screeningId));
        return "booking/grid";
    }
}
