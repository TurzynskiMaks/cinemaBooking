package pl.maksturzynski.cinemabooking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "film")
@Getter
@Setter
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 100)
    private String genre;

    @Column(name = "age_rating", nullable = false)
    private Integer ageRating;

    @Column(nullable = false, length = 150)
    private String director;

    @Column(name = "cast_text", nullable = false, length = 500)
    private String castText;

    @Column(name = "trailer_url", length = 500)
    private String trailerUrl;


}
