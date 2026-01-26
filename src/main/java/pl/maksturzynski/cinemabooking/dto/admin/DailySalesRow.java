package pl.maksturzynski.cinemabooking.dto.admin;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class DailySalesRow {
    private LocalDate day;
    private long ticketsCount;
    private BigDecimal revenue;

    public DailySalesRow(){}

    public DailySalesRow(LocalDate day, long ticketsCount, BigDecimal revenue) {
        this.day = day;
        this.ticketsCount = ticketsCount;
        this.revenue = revenue;
    }
}
