package pl.maksturzynski.cinemabooking.mapper;


import org.springframework.stereotype.Component;
import pl.maksturzynski.cinemabooking.domain.entity.Film;
import pl.maksturzynski.cinemabooking.dto.api.FilmResponse;
import pl.maksturzynski.cinemabooking.dto.api.FilmUpsertRequest;

@Component
public class FilmMapper {

    public Film toEntity(FilmUpsertRequest req) {
        Film film = new Film();
        film.setTitle(req.getTitle());
        film.setGenre(req.getGenre());
        film.setAgeRating(req.getAgeRating());
        film.setDirector(req.getDirector());
        film.setCastText(req.getCastText());
        film.setTrailerUrl(req.getTrailerUrl());
        return film;
    }

    public FilmResponse toResponse(Film film) {
        FilmResponse res = new FilmResponse();
        res.setId(film.getId());
        res.setTitle(film.getTitle());
        res.setGenre(film.getGenre());
        res.setAgeRating(film.getAgeRating());
        res.setDirector(film.getDirector());
        res.setCastText(film.getCastText());
        res.setTrailerUrl(film.getTrailerUrl());
        return res;
    }
}
