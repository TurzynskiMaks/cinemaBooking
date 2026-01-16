package pl.maksturzynski.cinemabooking.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.maksturzynski.cinemabooking.domain.entity.Film;
import pl.maksturzynski.cinemabooking.exception.FilmNotFoundException;
import pl.maksturzynski.cinemabooking.repository.FilmRepository;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Page<Film> findAll(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    public Film getById(Long id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new FilmNotFoundException(id));
    }

    public Film create(Film film) {
        return filmRepository.save(film);
    }

    public Film update(Long id, Film data) {
        Film existing = getById(id);

        existing.setTitle(data.getTitle());
        existing.setGenre(data.getGenre());
        existing.setTrailerUrl(data.getTrailerUrl());
        existing.setCastText(data.getCastText());
        existing.setAgeRating(data.getAgeRating());
        existing.setDirector(data.getDirector());

        return filmRepository.save(existing);
    }

    public void delete(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        filmRepository.deleteById(id);
    }
}
