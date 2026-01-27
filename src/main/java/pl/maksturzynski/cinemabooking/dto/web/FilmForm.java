package pl.maksturzynski.cinemabooking.dto.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmForm {
    @NotBlank @Size(max = 200) private String title;
    @NotBlank @Size(max = 100) private String genre;
    @NotNull private Integer ageRating;
    @NotBlank @Size(max = 150) private String director;
    @NotBlank @Size(max = 500) private String castText;
    @Size(max = 500) private String trailerUrl;
    private String imageUrls;
}
