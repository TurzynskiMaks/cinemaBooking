package pl.maksturzynski.cinemabooking.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class FilmUpsertRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    @Size(max = 100)
    private String genre;

    @NotNull
    private Integer ageRating;

    @NotBlank
    @Size(max = 150)
    private String director;

    @NotBlank
    @Size(max = 500)
    private String castText;

    @Size(max = 500)
    private String trailerUrl;
}
