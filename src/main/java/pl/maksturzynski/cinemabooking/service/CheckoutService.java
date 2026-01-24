package pl.maksturzynski.cinemabooking.service;


import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksturzynski.cinemabooking.domain.entity.*;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.repository.*;
import pl.maksturzynski.cinemabooking.web.vm.CartItemVm;
import pl.maksturzynski.cinemabooking.web.vm.CartVm;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CheckoutService {

    private final CartService cartService;
    private final SeatReservationRepository seatReservationRepository;
    private final BookingOrderRepository bookingOrderRepository;
    private final TicketRepository ticketRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final MailGateway mailGateway;

    public CheckoutService(CartService cartService,
                           SeatReservationRepository seatReservationRepository,
                           BookingOrderRepository bookingOrderRepository,
                           TicketRepository ticketRepository,
                           ScreeningRepository screeningRepository,
                           SeatRepository seatRepository, MailGateway mailGateway) {
        this.cartService = cartService;
        this.seatRepository = seatRepository;
        this.bookingOrderRepository = bookingOrderRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
        this.mailGateway = mailGateway;
    }

    @Transactional
    public BookingOrder pay(HttpSession session, String email) {
        CartVm cart = cartService.getOrCreateCart(session);
        if (cart.getItems().isEmpty()) {
            throw new BusinessException("Cart is empty!");
        }

        String sessionId = session.getId();
        LocalDateTime now = LocalDateTime.now();

        BookingOrder order = new BookingOrder();
        order.setOrderNumber("TCK-"+ UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setSessionId(sessionId);
        order.setTotalPrice(cart.getTotal());
        order.setStatus("PAID");
        order.setCreatedAt(now);

        order = bookingOrderRepository.save(order);

        for (CartItemVm it : cart.getItems()) {
            SeatReservation sr = seatReservationRepository
                    .findByScreeningIdAndSeatId(it.getScreeningId(), it.getSeatId())
                    .orElseThrow(() -> new BusinessException("No 'HOLD status' for seat: " + it.getSeatId()));

            if (sr.getStatus() == SeatStatus.SOLD) {
                throw new BusinessException("Seat already sold: " + it.getSeatId());
            }
            if (sr.getHeldUntil() != null && sr.getHeldUntil().isBefore(now)) {
                throw new BusinessException("'HOLD' status expired for seat: " + it.getSeatId());
            }
            if (sr.getHolderSessionId() == null || !sr.getHolderSessionId().equals(sessionId)) {
                throw new BusinessException("This seat is not HELD by Your session! (" + it.getSeatId() + ")");
            }

            sr.setStatus(SeatStatus.SOLD);
            sr.setHeldUntil(null);
            sr.setOrder(order);
            seatReservationRepository.save(sr);

            Screening screening = screeningRepository.findById(it.getScreeningId()).orElseThrow();
            Seat seat = seatRepository.findById(it.getSeatId()).orElseThrow();

            Ticket t = new Ticket();
            t.setOrder(order);
            t.setScreening(screening);
            t.setSeat(seat);
            t.setTicketType(it.getTicketType().name());
            t.setPrice(it.getUnitPrice());
            ticketRepository.save(t);
        }
        String ticketText = "ORDER: " + order.getOrderNumber() + "\nTOTAL: " + order.getTotalPrice() + "PLN\n";
        mailGateway.sendTicketEmail(email, order, ticketText);

        cartService.clear(session);
        return order;
    }
}
