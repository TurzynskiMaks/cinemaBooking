package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    void deleteByScreeningIdIn(List<Long> screeningIds);
}
