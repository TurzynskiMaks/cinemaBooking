package pl.maksturzynski.cinemabooking.web;


import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.maksturzynski.cinemabooking.service.FilmService;

@Controller
public class FilmWebController {

    private final FilmService filmService;

    public FilmWebController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public String films(Model model) {
        var page = filmService.findAll(PageRequest.of(0, 50));
        model.addAttribute("films", page.getContent());
        return "films/list";
    }

    @GetMapping("/films/{id}")
    public String filmDetails(@PathVariable Long id, Model model) {
        var film = filmService.getById(id);
        var images = filmService.getImagesForFilm(id);

        model.addAttribute("film", film);
        model.addAttribute("images", images);
        return "films/details";
    }
}
