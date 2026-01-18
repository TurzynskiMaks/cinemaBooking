package pl.maksturzynski.cinemabooking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket",
    uniqueConstraints = @UniqueConstraint(name = "uq_ticket", columnNames = {"screening_id", "seat_id"}))
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @Getter
    @Setter
    private BookingOrder order;

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

    @Column(name = "ticket_type", nullable = false)
    @Getter
    @Setter
    private String ticketType;

    @Column(nullable = false)
    @Getter
    @Setter
    private Integer price;
}
