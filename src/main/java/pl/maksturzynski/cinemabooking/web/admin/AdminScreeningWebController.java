package pl.maksturzynski.cinemabooking.web.admin;


import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.maksturzynski.cinemabooking.dto.web.ScreeningForm;
import pl.maksturzynski.cinemabooking.exception.BusinessException;
import pl.maksturzynski.cinemabooking.repository.FilmRepository;
import pl.maksturzynski.cinemabooking.repository.HallRepository;
import pl.maksturzynski.cinemabooking.repository.ScreeningRepository;
import pl.maksturzynski.cinemabooking.service.ScreeningService;

@Controller
@RequestMapping("/admin/screenings")
public class AdminScreeningWebController {

    private final ScreeningRepository screeningRepository;
    private final ScreeningService screeningService;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;

    public AdminScreeningWebController(ScreeningRepository screeningRepository,
                                       ScreeningService screeningService,
                                       FilmRepository filmRepository,
                                       HallRepository hallRepository) {
        this.screeningRepository = screeningRepository;
        this.screeningService = screeningService;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("screenings", screeningRepository.findAll());
        return "admin/screenings/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new ScreeningForm());
        model.addAttribute("films", filmRepository.findAll());
        model.addAttribute("halls", hallRepository.findAll());
        return "admin/screenings/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") ScreeningForm form,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("films", filmRepository.findAll());
            model.addAttribute("halls", hallRepository.findAll());
            return "admin/screenings/form";
        }
        try {
            screeningService.createWithOverlapCheck(form);
            return "redirect:/admin/screenings";
        } catch (BusinessException ex) {
            model.addAttribute("films", filmRepository.findAll());
            model.addAttribute("halls", hallRepository.findAll());
            model.addAttribute("globalError", ex.getMessage());
            return "admin/screenings/form";
        }
    }
}
