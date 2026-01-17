package pl.maksturzynski.cinemabooking.web;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.maksturzynski.cinemabooking.service.RepertoireService;

import java.time.LocalDate;

@Controller
public class RepertoireWebController {

    private final RepertoireService repertoireService;

    public RepertoireWebController(RepertoireService repertoireService) {
        this.repertoireService = repertoireService;
    }

    @GetMapping("/repertoire")
    public String repertoire(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            Model model
    ) {
        if (date == null) {
            date = LocalDate.now();
        }

        model.addAttribute("date", date);
        model.addAttribute("screenings", repertoireService.getScreeningsForDate(date));
        return "repertoire";
    }
}
