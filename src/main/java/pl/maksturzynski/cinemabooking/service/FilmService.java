package pl.maksturzynski.cinemabooking.service;


import org.springframework.stereotype.Service;
import pl.maksturzynski.cinemabooking.domain.entity.Film;
import pl.maksturzynski.cinemabooking.repository.FilmRepository;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }
}
