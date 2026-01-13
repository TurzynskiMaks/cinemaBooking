package pl.maksturzynski.cinemabooking.api;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.maksturzynski.cinemabooking.dto.api.FilmResponse;
import pl.maksturzynski.cinemabooking.dto.api.FilmUpsertRequest;
import pl.maksturzynski.cinemabooking.mapper.FilmMapper;
import pl.maksturzynski.cinemabooking.service.FilmService;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/films")
public class FilmApiController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;

    public FilmApiController(FilmService filmService, FilmMapper filmMapper) {
        this.filmService = filmService;
        this.filmMapper = filmMapper;
    }

    @GetMapping
    public Page<FilmResponse> list(Pageable pageable) {
        return filmService.findAll(pageable).map(filmMapper::toResponse);
    }

    @GetMapping("/{id}")
    public FilmResponse get(@PathVariable Long id) {
        return filmMapper.toResponse(filmService.getById(id));
    }

    @PostMapping
    public ResponseEntity<FilmResponse> create(
            @Valid @RequestBody FilmUpsertRequest request,
            UriComponentsBuilder uriBuilder
            ) {
        var created = filmService.create(filmMapper.toEntity(request));

        URI location = uriBuilder
                .path("/api/v1/films/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(filmMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public FilmResponse update(
            @PathVariable Long id,
            @Valid @RequestBody FilmUpsertRequest request
    ) {
        var updated = filmService.update(id, filmMapper.toEntity(request));
        return filmMapper.toResponse(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        filmService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
