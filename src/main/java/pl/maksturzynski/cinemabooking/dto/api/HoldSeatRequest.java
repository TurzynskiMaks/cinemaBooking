package pl.maksturzynski.cinemabooking.dto.api;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class HoldSeatRequest {

    @NotEmpty
    @Getter
    @Setter
    private List<Long> seatIds;
}
