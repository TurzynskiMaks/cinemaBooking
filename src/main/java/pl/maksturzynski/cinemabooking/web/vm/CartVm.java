package pl.maksturzynski.cinemabooking.web.vm;

import pl.maksturzynski.cinemabooking.domain.cart.TicketType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartVm implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<CartItemVm> items = new ArrayList<>();

    public List<CartItemVm> getItems() {
        return items;
    }

    public int getTotal() {
        return items.stream().mapToInt(CartItemVm::getUnitPrice).sum();
    }

    public void addOrReplace(CartItemVm item) {
        items.removeIf(x -> x.getKey().equals(item.getKey()));
        items.add(item);
    }

    public void updateTicketType(String key, TicketType type) {
        for (CartItemVm it : items) {
            if (it.getKey().equals(key)) {
                it.setTicketType(type);
                return;
            }
        }
    }

    public void remove(String key) {
        items.removeIf(x -> x.getKey().equals(key));
    }

    public void clear() {
        items.clear();
    }
}
