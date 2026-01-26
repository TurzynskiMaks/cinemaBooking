package pl.maksturzynski.cinemabooking.web.admin;


import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.maksturzynski.cinemabooking.domain.entity.Film;
import pl.maksturzynski.cinemabooking.dto.web.FilmForm;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.service.FilmService;

@Controller
@RequestMapping("/admin/films")
public class AdminFilmWebController {

    private final FilmService filmService;

    public AdminFilmWebController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("films", filmService.findAllSimple());
        return "admin/films/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new FilmForm());
        return "admin/films/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") FilmForm form, BindingResult result) {
        if (result.hasErrors()) return "admin/films/form";
        filmService.createFromForm(form);
        return "redirect:/admin/films";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Film film = filmService.getById(id);
        FilmForm filmForm = filmService.toForm(film);
        model.addAttribute("filmId", id);
        model.addAttribute("form", filmForm);
        return "admin/films/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") FilmForm form,
                         BindingResult result,
                         Model model){
        if (result.hasErrors()) {
            model.addAttribute("filmId", id);
            return "admin/films/form";
        }
        filmService.updateFromForm(id, form);
        return "redirect:/admin/films";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable long id, RedirectAttributes redir) {
        try {
            filmService.delete(id);
            redir.addFlashAttribute("success", "Film removed.");
        } catch (BusinessException ex) {
            redir.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/films";

    }
}
