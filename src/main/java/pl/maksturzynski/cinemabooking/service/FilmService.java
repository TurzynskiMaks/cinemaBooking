package pl.maksturzynski.cinemabooking.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import pl.maksturzynski.cinemabooking.domain.entity.Film;
import pl.maksturzynski.cinemabooking.domain.entity.FilmImage;
import pl.maksturzynski.cinemabooking.domain.entity.Screening;
import pl.maksturzynski.cinemabooking.dto.api.MovieExternalDto;
import pl.maksturzynski.cinemabooking.dto.web.FilmForm;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.exception.FilmNotFoundException;
import pl.maksturzynski.cinemabooking.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmImageRepository filmImageRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatReservationRepository seatReservationRepository;
    private final TicketRepository ticketRepository;
    private final RestClient restClient;

    public FilmService(FilmRepository filmRepository,
                       FilmImageRepository filmImageRepository,
                       ScreeningRepository screeningRepository,
                       SeatReservationRepository seatReservationRepository,
                       TicketRepository ticketRepository,
                       RestClient restClient) {
        this.filmRepository = filmRepository;
        this.filmImageRepository = filmImageRepository;
        this.screeningRepository = screeningRepository;
        this.seatReservationRepository = seatReservationRepository;
        this.ticketRepository = ticketRepository;
        this.restClient = restClient;
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
        applyForm(film, form);
        film = filmRepository.save(film);
        syncImages(film, form.getImageUrls());
        return film;
    }

    public FilmForm toForm(Film film) {
        FilmForm form = new FilmForm();
        form.setTitle(film.getTitle());
        form.setGenre(film.getGenre());
        form.setAgeRating(film.getAgeRating());
        form.setDirector(film.getDirector());
        form.setCastText(film.getCastText());
        form.setTrailerUrl(film.getTrailerUrl());

        var images = filmImageRepository.findByFilmIdOrderBySortOrderAsc(film.getId());
        String joined = images.stream()
                .map(FilmImage::getUrl)
                .reduce((a,b) -> a + "\n" + b)
                .orElse("");
        form.setImageUrls(joined);
        return form;
    }

    @Transactional
    public Film updateFromForm(Long id, FilmForm form) {
        Film existing = getById(id);

        applyForm(existing, form);
        existing = filmRepository.save(existing);
        syncImages(existing, form.getImageUrls());
        return existing;
    }

    private void applyForm(Film film, FilmForm form) {
        film.setTitle(form.getTitle());
        film.setGenre(form.getGenre());
        film.setTrailerUrl(form.getTrailerUrl());
        film.setCastText(form.getCastText());
        film.setAgeRating(form.getAgeRating());
        film.setDirector(form.getDirector());
    }

    private void syncImages(Film film, String imageUrlsRaw) {
        filmImageRepository.deleteByFilmId(film.getId());

        List<String> urls = parseUrls(imageUrlsRaw);
        int i = 1;
        for (String url : urls) {
            FilmImage img = new FilmImage();
            img.setFilm(film);
            img.setUrl(url);
            img.setSortOrder(i++);
            filmImageRepository.save(img);
        }
    }

    private List<String> parseUrls(String raw) {
        List<String> out = new ArrayList<>();
        if (raw == null) return out;

        for (String line : raw.split("\\R")) {
            String u = line.trim();
            if (!u.isEmpty()) out.add(u);
        }
        return out;
    }

    @Transactional
    public Film importFromExternalApi(String title) {
        if (filmRepository.existsByTitle(title)) {
            throw new BusinessException("Film o tytule '" + title + "' już istnieje w bazie!");
        }
        MovieExternalDto dto = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("t", title)
                        .queryParam("apikey", "e5973f8a")
                        .build())
                .retrieve()
                .body(MovieExternalDto.class);
        if (dto == null || "False".equals(dto.response())) {
            throw new BusinessException("Nie znaleziono filmu o tytule: " + title);
        }


        Film film = new Film();
        film.setTitle(dto.title());
        film.setGenre(dto.genre());
        film.setDirector(dto.director());
        film.setCastText(dto.actors());
        film.setAgeRating(extractAge(dto.rated()));


        film = filmRepository.save(film);


        if (dto.posterUrl() != null && !dto.posterUrl().equals("N/A")) {
            syncImages(film, dto.posterUrl());
        }

        return film;
    }


    private Integer extractAge(String rated) {
        if (rated == null || rated.equals("N/A")) return 12;

        String digits = rated.replaceAll("\\D+", "");
        return digits.isEmpty() ? 12 : Integer.parseInt(digits);
    }
    public boolean existsByTitle(String title) {
        return filmRepository.existsByTitle(title);
    }

}
