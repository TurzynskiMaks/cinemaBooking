package pl.maksturzynski.cinemabooking.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false, length = 200)
    @Getter
    @Setter
    private String title;

    @Column(nullable = false, length = 100)
    @Getter
    @Setter
    private String genre;

    @Column(name = "age_rating", nullable = false)
    @Getter
    @Setter
    private Integer ageRating;

    @Column(nullable = false, length = 150)
    @Getter
    @Setter
    private String director;

    @Column(name = "cast_text", nullable = false, length = 500)
    @Getter
    @Setter
    private String castText;

    @Column(name = "trailer_url", length = 500)
    @Getter
    @Setter
    private String trailerUrl;

    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @Getter
    private List<FilmImage> images = new ArrayList<>();


}
