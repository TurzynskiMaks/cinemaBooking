package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
