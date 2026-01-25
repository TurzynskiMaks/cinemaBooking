package pl.maksturzynski.cinemabooking.web.vm;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class AddToCartRequest {
    private Long screeningId;
    private List<Long> seatIds;
    private String ticketType;
}
