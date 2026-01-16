package pl.maksturzynski.cinemabooking.domain.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(name = "row_num", nullable = false)
    private Integer rowNum;

    @Column(name = "seat_num", nullable = false)
    private Integer seatNum;

    public Long getId() { return id; }

    public Hall getHall() { return hall; }
    public void setHall(Hall hall) { this.hall = hall; }

    public Integer getRowNum() { return rowNum; }
    public void setRowNum(Integer rowNum) { this.rowNum = rowNum; }

    public Integer getSeatNum() { return seatNum; }
    public void setSeatNum(Integer seatNum) { this.seatNum = seatNum; }
}
