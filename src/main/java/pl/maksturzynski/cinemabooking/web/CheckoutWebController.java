package pl.maksturzynski.cinemabooking.web;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.maksturzynski.cinemabooking.service.CartService;
import pl.maksturzynski.cinemabooking.service.CheckoutService;

@Slf4j
@Controller
public class CheckoutWebController {

    private final CartService cartService;
    private final CheckoutService checkoutService;

    public CheckoutWebController(CartService cartService, CheckoutService checkoutService) {
        this.cartService = cartService;
        this.checkoutService = checkoutService;
    }

    @GetMapping("/checkout")
    public String checkout(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getOrCreateCart(session));
        return "checkout/summary";
    }

    @PostMapping("/checkout/confirm")
    public String confirm(Model model,
                          HttpSession session,
                          @RequestParam(required = false) String email) {
        var order = checkoutService.pay(session, email);
        model.addAttribute("order", order);
        return "checkout/success";
    }
}
