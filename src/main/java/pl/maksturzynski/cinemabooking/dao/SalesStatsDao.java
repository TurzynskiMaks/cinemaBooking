package pl.maksturzynski.cinemabooking.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pl.maksturzynski.cinemabooking.dto.admin.DailySalesRow;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SalesStatsDao {

    private final JdbcTemplate jdbc;

    public SalesStatsDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<DailySalesRow> salesByDay(LocalDateTime from, LocalDateTime to) {
        String sql = """
                SELECT CAST(created_at AS DATE) AS day,
                    COUNT(*) AS tickets_count,
                    COALESCE(SUM(total_price), 0) AS revenue
                FROM booking_order
                WHERE status = 'PAID'
                    AND created_at >= ?
                    AND created_at < ?
                GROUP BY CAST(created_at AS DATE)
                ORDER BY day
                """;

        RowMapper<DailySalesRow> mapper = (ResultSet results, int rowNum) -> {
            LocalDate day = results.getDate("day").toLocalDate();
            long tickets = results.getLong("tickets_count");
            BigDecimal revenue = results.getBigDecimal("revenue");
            return new DailySalesRow(day, tickets, revenue);
        };

        return jdbc.query(sql, mapper, Timestamp.valueOf(from), Timestamp.valueOf(to));
    }
}
