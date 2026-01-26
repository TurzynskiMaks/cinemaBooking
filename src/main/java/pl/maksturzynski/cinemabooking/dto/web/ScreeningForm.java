package pl.maksturzynski.cinemabooking.dto.web;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScreeningForm {
    @NotNull private Long filmId;
    @NotNull private Long hallId;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @NotNull @Min(1)
    private Integer durationMinutes;
}
