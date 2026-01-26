package pl.maksturzynski.cinemabooking.web.admin;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminHomeWebController {

    @GetMapping
    public String index() {
        return "admin/index";
    }
}
