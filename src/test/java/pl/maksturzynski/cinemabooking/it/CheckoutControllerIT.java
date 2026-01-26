package pl.maksturzynski.cinemabooking.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;
import pl.maksturzynski.cinemabooking.domain.entity.*;
import pl.maksturzynski.cinemabooking.repository.*;
import pl.maksturzynski.cinemabooking.service.MailGateway;
import pl.maksturzynski.cinemabooking.web.vm.CartItemVm;
import pl.maksturzynski.cinemabooking.web.vm.CartVm;

import java.time.LocalDateTime;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.flyway.enabled=true"
})
@AutoConfigureMockMvc
class CheckoutControllerIT {

    @Autowired MockMvc mvc;

    @Autowired FilmRepository filmRepository;
    @Autowired ScreeningRepository screeningRepository;
    @Autowired SeatRepository seatRepository;
    @Autowired SeatReservationRepository seatReservationRepository;
    @Autowired HallRepository hallRepository;

    @MockitoBean MailGateway mailGateway;


    @Test
    void postCheckoutConfirm_marksSold_andReturnsSuccessView() throws Exception {
        doNothing().when(mailGateway).sendTicketEmail(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());

        Hall hall = new Hall();
        hall.setName("Sala 1");
        hall = hallRepository.save(hall);

        Film film = new Film();
        film.setTitle("IT Film");
        film.setGenre("X");
        film.setDirector("Y");
        film.setAgeRating(12);
        film.setCastText("Z");
        film = filmRepository.save(film);


        Screening screening = new Screening();
        screening.setHall(hall);
        screening.setFilm(film);
        screening.setStartTime(LocalDateTime.now().plusDays(1));
        screening.setDurationMinutes(120);
        screening = screeningRepository.save(screening);

        Seat seat = new Seat();
        seat.setHall(hall);
        seat.setRowNum(1);
        seat.setSeatNum(1);
        seat = seatRepository.save(seat);

        MockHttpSession session = new MockHttpSession();
        CartVm cart = new CartVm();
        CartItemVm item = new CartItemVm();
        item.setKey(screening.getId() + ":" + seat.getId());
        item.setScreeningId(screening.getId());
        item.setSeatId(seat.getId());
        item.setTicketType(TicketType.NORMAL);
        cart.addOrReplace(item);
        session.setAttribute("CART", cart);

        SeatReservation sr = new SeatReservation();
        sr.setScreening(screening);
        sr.setSeat(seat);
        sr.setStatus(SeatStatus.HELD);
        sr.setHolderSessionId(session.getId());
        sr.setHeldUntil(LocalDateTime.now().plusMinutes(10));
        seatReservationRepository.save(sr);

        mvc.perform(post("/checkout/confirm")
                        .session(session)
                        .param("email", "it@test.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("checkout/success"))
                .andExpect(model().attributeExists("order"));
    }
}
