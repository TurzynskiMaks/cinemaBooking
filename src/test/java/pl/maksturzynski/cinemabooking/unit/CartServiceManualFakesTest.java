package pl.maksturzynski.cinemabooking.unit;

import org.junit.jupiter.api.Test;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;
import pl.maksturzynski.cinemabooking.service.CartService;
import pl.maksturzynski.cinemabooking.unit.fakes.FakeHttpSession;
import pl.maksturzynski.cinemabooking.web.vm.CartItemVm;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceManualFakesTest {

    @Test
    void getOrCreateCart_createsAndStoresCartInSession() {
        FakeHttpSession session = new FakeHttpSession();

        // repozytoria nie są tu używane -> mogą być null, bo testujemy część sesyjną
        CartService cartService = new CartService(null, null);

        var cart1 = cartService.getOrCreateCart(session);
        var cart2 = cartService.getOrCreateCart(session);

        assertNotNull(cart1);
        assertSame(cart1, cart2);
        assertTrue(cart1.getItems().isEmpty());
    }

    @Test
    void updateRemoveClear_workOnSessionCart() {
        FakeHttpSession session = new FakeHttpSession();
        CartService cartService = new CartService(null, null);

        var cart = cartService.getOrCreateCart(session);

        CartItemVm a = new CartItemVm();
        a.setKey("1:10");
        a.setTicketType(TicketType.NORMAL);

        CartItemVm b = new CartItemVm();
        b.setKey("1:11");
        b.setTicketType(TicketType.NORMAL);

        cart.addOrReplace(a);
        cart.addOrReplace(b);

        assertEquals(2, cart.getItems().size());

        cartService.updateType(session, "1:10", TicketType.DISCOUNT);
        assertEquals(TicketType.DISCOUNT,
                cart.getItems().stream().filter(x -> x.getKey().equals("1:10")).findFirst().orElseThrow().getTicketType());

        cartService.remove(session, "1:11");
        assertEquals(1, cart.getItems().size());

        cartService.clear(session);
        assertEquals(0, cart.getItems().size());
    }
}
