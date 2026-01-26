package pl.maksturzynski.cinemabooking.unit;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;
import pl.maksturzynski.cinemabooking.domain.entity.*;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.repository.*;
import pl.maksturzynski.cinemabooking.service.*;
import pl.maksturzynski.cinemabooking.web.vm.CartItemVm;
import pl.maksturzynski.cinemabooking.web.vm.CartVm;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CheckoutServiceMockitoTest {

    @Mock CartService cartService;
    @Mock SeatReservationRepository seatReservationRepository;
    @Mock BookingOrderRepository bookingOrderRepository;
    @Mock TicketRepository ticketRepository;
    @Mock ScreeningRepository screeningRepository;
    @Mock SeatRepository seatRepository;
    @Mock MailGateway mailGateway;

    @InjectMocks CheckoutService checkoutService;

    @Test
    void pay_happyPath_setsSold_createsTickets_sendsMail_clearsCart() {
        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn("S1");

        CartVm cart = new CartVm();
        CartItemVm item = new CartItemVm();
        item.setKey("5:100");
        item.setScreeningId(5L);
        item.setSeatId(100L);
        item.setTicketType(TicketType.NORMAL);
        cart.addOrReplace(item);

        when(cartService.getOrCreateCart(session)).thenReturn(cart);

        SeatReservation sr = new SeatReservation();
        sr.setStatus(SeatStatus.HELD);
        sr.setHolderSessionId("S1");
        sr.setHeldUntil(LocalDateTime.now().plusMinutes(5));
        when(seatReservationRepository.findByScreeningIdAndSeatId(5L, 100L)).thenReturn(Optional.of(sr));

        Screening screening = new Screening();
        Film film = new Film();
        film.setTitle("Test Film");
        screening.setFilm(film);
        when(screeningRepository.findById(5L)).thenReturn(Optional.of(screening));

        Seat seat = new Seat();
        seat.setRowNum(1);
        seat.setSeatNum(1);
        when(seatRepository.findById(100L)).thenReturn(Optional.of(seat));

        when(bookingOrderRepository.save(any(BookingOrder.class))).thenAnswer(inv -> {
            BookingOrder o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        BookingOrder order = checkoutService.pay(session, "a@b.com");

        assertNotNull(order.getOrderNumber());
        assertEquals("PAID", order.getStatus());

        verify(seatReservationRepository).save(argThat(x -> x.getStatus() == SeatStatus.SOLD));
        verify(ticketRepository).save(any(Ticket.class));
        verify(mailGateway).sendTicketEmail(eq("a@b.com"), any(BookingOrder.class), anyString());
        verify(cartService).clear(session);
    }

    @Test
    void pay_throwsWhenSeatHeldByOtherSession() {
        HttpSession session = mock(HttpSession.class);
        when(session.getId()).thenReturn("S1");

        CartVm cart = new CartVm();
        CartItemVm item = new CartItemVm();
        item.setKey("5:100");
        item.setScreeningId(5L);
        item.setSeatId(100L);
        item.setTicketType(TicketType.NORMAL);
        cart.addOrReplace(item);

        when(cartService.getOrCreateCart(session)).thenReturn(cart);

        SeatReservation sr = new SeatReservation();
        sr.setStatus(SeatStatus.HELD);
        sr.setHolderSessionId("OTHER");
        sr.setHeldUntil(LocalDateTime.now().plusMinutes(5));
        when(seatReservationRepository.findByScreeningIdAndSeatId(5L, 100L)).thenReturn(Optional.of(sr));

        assertThrows(BusinessException.class, () -> checkoutService.pay(session, "a@b.com"));

        verify(bookingOrderRepository, never()).save(any());
        verify(ticketRepository, never()).save(any());
        verify(mailGateway, never()).sendTicketEmail(any(), any(), anyString());
    }
}
