package pl.maksturzynski.cinemabooking.web.vm;

import lombok.Getter;
import lombok.Setter;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class CartItemVm implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;
    private Long screeningId;
    private String filmTitle;
    private LocalDateTime startTime;

    private Long seatId;
    private Integer rowNum;
    private Integer seatNum;

    private TicketType ticketType;

    public int getUnitPrice() {
        return ticketType.getPrice();
    }
}
