package pl.maksturzynski.cinemabooking.web.admin;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.maksturzynski.cinemabooking.service.SalesStatsService;

import java.time.YearMonth;

@Controller
@RequestMapping("/admin/stats")
public class AdminStatsWebController {

    private final SalesStatsService service;

    public AdminStatsWebController(SalesStatsService service) {
        this.service = service;
    }

    @GetMapping
    public String month(@RequestParam(required = false) String month, Model model) {
        YearMonth yearMonth = (month == null || month.isBlank())
                ? YearMonth.now()
                : YearMonth.parse(month);
        var rows = service.monthReport(yearMonth);

        model.addAttribute("month", yearMonth.toString());
        model.addAttribute("rows", rows);
        return "admin/stats/month";
    }
}
