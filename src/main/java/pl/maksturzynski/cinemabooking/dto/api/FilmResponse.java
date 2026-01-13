package pl.maksturzynski.cinemabooking.dto.api;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmResponse {

    private Long id;
    private String title;
    private String genre;
    private Integer ageRating;
    private String director;
    private String castText;
    private String trailerUrl;
}
