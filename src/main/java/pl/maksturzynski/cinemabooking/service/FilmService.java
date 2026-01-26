package pl.maksturzynski.cinemabooking.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.maksturzynski.cinemabooking.domain.entity.Film;
import pl.maksturzynski.cinemabooking.domain.entity.FilmImage;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.dto.web.FilmForm;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.exception.FilmNotFoundException;
import pl.maksturzynski.cinemabooking.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmImageRepository filmImageRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final TicketRepository ticketRepository;

    public FilmService(FilmRepository filmRepository, FilmImageRepository filmImageRepository, ScreeningRepository screeningRepository, SeatReservationRepository seatReservationRepository, TicketRepository ticketRepository) {
        this.filmRepository = filmRepository;
        this.filmImageRepository = filmImageRepository;
        this.screeningRepository = screeningRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.ticketRepository = ticketRepository;
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

    @Transactional
    public void delete(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new FilmNotFoundException(id);
        }
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();

        boolean hasScreeningsAhead = screeningRepository
                .existsByFilmIdAndStartTimeGreaterThanEqual(id, todayStart);

        if (hasScreeningsAhead) {
            throw new BusinessException("Film has screenings ahead!");
        }
        List<Long> screeningIds = screeningRepository.findAllByFilmId(id).stream()
                        .map(Screening::getId)
                                .toList();
        if (!screeningIds.isEmpty()) {
            seatReservationRepository.deleteByScreeningIdIn(screeningIds);
            ticketRepository.deleteByScreeningIdIn(screeningIds);

            screeningRepository.deleteAllByIdInBatch(screeningIds);
        }

        filmRepository.deleteById(id);
    }

    public Film getFilmDetails(Long id) {
        return getById(id);
    }

    public List<FilmImage> getImagesForFilm(Long filmId) {
        return filmImageRepository.findByFilmIdOrderBySortOrderAsc(filmId);
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public List<Film> findAllSimple() {
        return filmRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Transactional
    public Film createFromForm(FilmForm form) {
        Film film = new Film();
        film.setTitle(form.getTitle());
        film.setGenre(form.getGenre());
        film.setAgeRating(form.getAgeRating());
        film.setDirector(form.getDirector());
        film.setCastText(form.getCastText());
        film.setTrailerUrl(form.getTrailerUrl());

        return filmRepository.save(film);
    }

    public FilmForm toForm(Film film) {
        FilmForm form = new FilmForm();
        form.setTitle(film.getTitle());
        form.setGenre(film.getGenre());
        form.setAgeRating(film.getAgeRating());
        form.setDirector(film.getDirector());
        form.setCastText(film.getCastText());
        form.setTrailerUrl(film.getTrailerUrl());
        return form;
    }

    @Transactional
    public Film updateFromForm(Long id, FilmForm form) {
        Film existing = getById(id);

        existing.setTitle(form.getTitle());
        existing.setGenre(form.getGenre());
        existing.setAgeRating(form.getAgeRating());
        existing.setDirector(form.getDirector());
        existing.setCastText(form.getCastText());
        existing.setTrailerUrl(form.getTrailerUrl());

        return filmRepository.save(existing);
    }
}
