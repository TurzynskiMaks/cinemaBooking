package pl.maksturzynski.cinemabooking.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import pl.maksturzynski.cinemabooking.config.MailProperties;
import pl.maksturzynski.cinemabooking.domain.entity.BookingOrder;
import pl.maksturzynski.cinemabooking.exception.BusinessException;

@Slf4j
@Service
public class MailGateway {

    private final MailProperties properties;
    private final RestClient restClient;

    public MailGateway(MailProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public void sendTicketEmail(String targetEmail, BookingOrder order, String ticketText) {
        if (properties.getApiKey() == null || properties.getApiKey().isBlank()) {
            throw new BusinessException("No MAIL_API_KEY provided!");
        }
        if (properties.getBaseUrl() == null || properties.getBaseUrl().isBlank()){
            throw new BusinessException("No MAIL_API_URL provided!");
        }

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("target", targetEmail);
        form.add("subject", "Cinematron - Zakup biletów");
        form.add("content", "Dziękujemy! Twój numer rezerwacji: " + order.getOrderNumber() + "\n" + ticketText);
        form.add("key", properties.getApiKey());

        try {
            restClient.post()
                    .uri("/send")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(form)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new BusinessException("Something went terribly wrong" + e);
        }
    }
}
