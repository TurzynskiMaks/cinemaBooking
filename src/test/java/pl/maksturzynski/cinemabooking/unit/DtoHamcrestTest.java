package pl.maksturzynski.cinemabooking.unit;

import org.junit.jupiter.api.Test;
import pl.maksturzynski.cinemabooking.domain.cart.TicketType;
import pl.maksturzynski.cinemabooking.web.vm.CartItemVm;
import pl.maksturzynski.cinemabooking.web.vm.CartVm;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class DtoHamcrestTest {

    @Test
    void cartItemVm_gettersSetters() {
        CartItemVm vm = new CartItemVm();
        vm.setKey("1:2");
        vm.setTicketType(TicketType.DISCOUNT);

        assertThat(vm, hasProperty("key", equalTo("1:2")));
        assertThat(vm, hasProperty("ticketType", equalTo(TicketType.DISCOUNT)));
        assertThat(vm.getUnitPrice(), equalTo(TicketType.DISCOUNT.getPrice()));
    }

    @Test
    void cartVm_totalSumsUnitPrices() {
        CartVm cart = new CartVm();

        CartItemVm a = new CartItemVm(); a.setKey("a"); a.setTicketType(TicketType.NORMAL);
        CartItemVm b = new CartItemVm(); b.setKey("b"); b.setTicketType(TicketType.DISCOUNT);
        cart.addOrReplace(a);
        cart.addOrReplace(b);

        assertThat(cart.getItems(), hasSize(2));
        assertThat(cart.getTotal(), equalTo(TicketType.NORMAL.getPrice() + TicketType.DISCOUNT.getPrice()));
    }
}
