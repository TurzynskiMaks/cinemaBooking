package pl.maksturzynski.cinemabooking.web;


import org.springframework.data.domain.PageRequest;
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
        var page = filmService.findAll(PageRequest.of(0, 50));
        model.addAttribute("films", page.getContent());
        return "films/list";
    }
}
