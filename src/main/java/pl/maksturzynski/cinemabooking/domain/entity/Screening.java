package pl.maksturzynski.cinemabooking.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "screening")
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    public Long getId() { return id; }

    public Film getFilm() { return film; }
    public void setFilm(Film film) { this.film = film; }

    public Hall getHall() { return hall; }
    public void setHall(Hall hall) { this.hall = hall; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}
