package pl.maksturzynski.cinemabooking.service;


import org.springframework.stereotype.Service;
import pl.maksturzynski.cinemabooking.dao.SalesStatsDao;
import pl.maksturzynski.cinemabooking.dto.admin.DailySalesRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SalesStatsService {

    private final SalesStatsDao dao;

    public SalesStatsService(SalesStatsDao dao) {
        this.dao = dao;
    }

    public List<DailySalesRow> monthReport(YearMonth yearMonth) {
        LocalDateTime from = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime to = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        List<DailySalesRow> raw = dao.salesByDay(from, to);
        Map<LocalDate, DailySalesRow> byDay = new HashMap<>();
        for (var r : raw) byDay.put(r.getDay(), r);

        List<DailySalesRow> out = new ArrayList<>();
        for (int d = 1; d <= yearMonth.lengthOfMonth(); d++) {
            LocalDate day = yearMonth.atDay(d);
            out.add(byDay.getOrDefault(day, new DailySalesRow(day, 0, BigDecimal.ZERO)));
        }
        return out;
    }
}
