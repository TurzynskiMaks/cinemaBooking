package pl.maksturzynski.cinemabooking.exception;

public class FilmNotFoundException extends RuntimeException {
    public FilmNotFoundException(Long id) {
        super("Film not found: " + id);
    }
}
