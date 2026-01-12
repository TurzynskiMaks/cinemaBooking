package pl.maksturzynski.cinemabooking.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.maksturzynski.cinemabooking.service.FilmService;

@Controller
public class FilmWebController {

    private final FilmService filmService;

    public FilmWebController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public String films(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films/list";
    }
}
