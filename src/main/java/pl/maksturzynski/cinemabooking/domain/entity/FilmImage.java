package pl.maksturzynski.cinemabooking.domain.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "film_image")
public class FilmImage {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @Getter
    @Setter
    @Column(nullable = false, length = 500)
    private String url;

    @Getter
    @Setter
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
