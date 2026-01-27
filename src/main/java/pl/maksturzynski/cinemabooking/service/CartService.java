package pl.maksturzynski.cinemabooking.service;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.domain.entity.Seat;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.repository.ScreeningRepository;
import pl.maksturzynski.cinemabooking.repository.SeatRepository;
import pl.maksturzynski.cinemabooking.web.vm.CartItemVm;
import pl.maksturzynski.cinemabooking.web.vm.CartVm;

@Slf4j
@Service
public class CartService {

    private static final String CART_KEY = "CART";

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    public CartService(ScreeningRepository screeningRepository, SeatRepository seatRepository) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
    }

    public CartVm getOrCreateCart(HttpSession session) {
        CartVm cart = (CartVm) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new CartVm();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    public void add(HttpSession session, Long screeningId, Long seatId, TicketType type) {
        CartVm cart = getOrCreateCart(session);
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new BusinessException("Screening not found: " + screeningId));
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new BusinessException("Seat not found: " + seatId));

        CartItemVm item = new CartItemVm();
        item.setKey(screeningId + ":" + seatId);
        item.setScreeningId(screeningId);
        item.setFilmTitle(screening.getFilm().getTitle());
        item.setStartTime(screening.getStartTime());

        item.setSeatId(seatId);
        item.setRowNum(seat.getRowNum());
        item.setSeatNum(seat.getSeatNum());

        item.setTicketType(type);

        getOrCreateCart(session).addOrReplace(item);
        session.setAttribute(CART_KEY, cart);
    }

    public void updateType(HttpSession session, String key, TicketType type) {
        getOrCreateCart(session).updateTicketType(key, type);
        CartVm cart = getOrCreateCart(session);
        session.setAttribute(CART_KEY, cart);
    }

    public void remove(HttpSession session, String key) {
        getOrCreateCart(session).remove(key);
        CartVm cart = getOrCreateCart(session);
        session.setAttribute(CART_KEY, cart);
    }

    public void clear(HttpSession session) {
        getOrCreateCart(session).clear();
        CartVm cart = getOrCreateCart(session);
        session.setAttribute(CART_KEY, cart);
    }
}
