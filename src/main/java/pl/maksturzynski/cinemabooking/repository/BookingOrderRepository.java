package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.BookingOrder;

public interface BookingOrderRepository extends JpaRepository<BookingOrder, Long> {
}
