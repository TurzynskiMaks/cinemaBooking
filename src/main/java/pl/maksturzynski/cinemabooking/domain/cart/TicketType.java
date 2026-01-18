package pl.maksturzynski.cinemabooking.domain.cart;

import lombok.Getter;

public enum TicketType {
    NORMAL("Normalny", 35),
    DISCOUNT("Ulgowy", 28),
    FAMILY("Rodzinny", 25);

    @Getter
    private final String label;

    @Getter
    private final int price;

    TicketType(String label, int price) {
        this.label = label;
        this.price = price;
    }
}
