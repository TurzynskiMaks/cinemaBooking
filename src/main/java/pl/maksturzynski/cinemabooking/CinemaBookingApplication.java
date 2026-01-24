package pl.maksturzynski.cinemabooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(pl.maksturzynski.cinemabooking.config.MailProperties.class)
public class CinemaBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaBookingApplication.class, args);
	}

}
