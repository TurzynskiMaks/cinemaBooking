package pl.maksturzynski.cinemabooking.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.maksturzynski.cinemabooking.dao.SalesStatsDao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb2;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.flyway.enabled=true"
})
class SalesStatsDaoIT {

    @Autowired JdbcTemplate jdbc;
    @Autowired SalesStatsDao dao;

    @Test
    void salesByDay_returnsRows() {
        jdbc.update("insert into booking_order(id, order_number, session_id, total_price, status, created_at) values (1,'TCK-AAAA','S',100,'PAID', CURRENT_TIMESTAMP)");
        jdbc.update("insert into ticket(id, order_id, ticket_type, price) values (10,1,'NORMAL',50)");
        jdbc.update("insert into ticket(id, order_id, ticket_type, price) values (11,1,'NORMAL',50)");

        var from = LocalDateTime.now().minusDays(1);
        var to = LocalDateTime.now().plusDays(1);

        var rows = dao.salesByDay(from, to);

        assertFalse(rows.isEmpty());

        assertTrue(rows.get(0).getTicketsCount() >= 2);
        assertTrue(rows.get(0).getRevenue().compareTo(new BigDecimal("100")) >= 0);
    }
}
