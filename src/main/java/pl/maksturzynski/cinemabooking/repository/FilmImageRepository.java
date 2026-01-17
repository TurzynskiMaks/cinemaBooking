package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.FilmImage;

import java.util.List;

public interface FilmImageRepository extends JpaRepository<FilmImage, Long> {
    List<FilmImage> findByFilmIdOrder(Long filmId);
}
