package pl.maksturzynski.cinemabooking.web;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;
import pl.maksturzynski.cinemabooking.service.CartService;
import pl.maksturzynski.cinemabooking.web.vm.AddToCartRequest;

@Controller
public class CartWebController {

    private final CartService cartService;

    public CartWebController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String cart(Model model, HttpSession session) {
        model.addAttribute("cart", cartService.getOrCreateCart(session));
        model.addAttribute("ticketTypes", TicketType.values());
        return "cart";
    }

    @PostMapping("/cart/add")
    public String add(@RequestParam long screeningId,
                      @RequestParam long seatId,
                      @RequestParam(defaultValue = "NORMAL") TicketType ticketType,
                      HttpSession session) {
        cartService.add(session, screeningId, seatId, ticketType);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update")
    public String update(@RequestParam String key,
                         @RequestParam TicketType ticketType,
                         HttpSession session) {
        cartService.updateType(session, key, ticketType);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove")
    public String remove(@RequestParam String key,
                         HttpSession session) {
        cartService.remove(session, key);
        return "redirect:/cart";
    }

    @PostMapping("/cart/clear")
    public String clear(HttpSession session) {
        cartService.clear(session);
        return "redirectL/cart";
    }

    @PostMapping("/cart/add-many")
    @ResponseBody
    public ResponseEntity<Void> addMany(@RequestBody AddToCartRequest req, HttpSession session) {
        var type = (req.getTicketType() == null || req.getTicketType().isBlank())
                ? TicketType.NORMAL
                : TicketType.valueOf(req.getTicketType());

        if (req.getSeatIds() != null) {
            for (Long seatId : req.getSeatIds()) {
                cartService.add(session, req.getScreeningId(), seatId, type);
            }
        }
        return ResponseEntity.noContent().build();
    }
}
