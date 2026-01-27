package pl.maksturzynski.cinemabooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.maksturzynski.cinemabooking.domain.entity.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {
    boolean existsByTitle(String title);
}
