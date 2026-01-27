package pl.maksturzynski.cinemabooking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_order")
public class BookingOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    @Getter
    @Setter
    private String orderNumber;

    @Column(name = "session_id", nullable = false, length = 100)
    @Getter
    @Setter
    private String sessionId;

    @Column(name = "total_price", nullable = false)
    @Getter
    @Setter
    private Integer totalPrice;

    @Column(nullable = false, length = 20)
    @Getter
    @Setter
    private String status;

    @Column(name = "created_at", nullable = false)
    @Getter
    @Setter
    private LocalDateTime createdAt;
}
