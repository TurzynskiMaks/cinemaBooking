package pl.maksturzynski.cinemabooking.dto.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatStatusResponse {
    private Long seatId;
    private Integer rowNum;
    private Integer seatNum;
    private String status;
}
