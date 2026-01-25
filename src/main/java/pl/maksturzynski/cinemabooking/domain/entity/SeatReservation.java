package pl.maksturzynski.cinemabooking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "seat_reservation",
uniqueConstraints = @UniqueConstraint(name = "uq_sr", columnNames = {"screening_id", "seat_id"}))
public class SeatReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_id", nullable = false)
    @Getter
    @Setter
    private Screening screening;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    @Getter
    @Setter
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Getter
    @Setter
    private SeatStatus status;

    @Column(name = "held_until")
    @Getter
    @Setter
    private LocalDateTime heldUntil;

    @Column(name = "holder_session_id", length = 100)
    @Getter
    @Setter
    private String holderSessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @Getter
    @Setter
    private BookingOrder order;
}
