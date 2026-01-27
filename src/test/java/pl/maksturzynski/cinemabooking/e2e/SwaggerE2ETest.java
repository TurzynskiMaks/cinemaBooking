package pl.maksturzynski.cinemabooking.e2e;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwaggerE2ETest {

    @LocalServerPort int port;

    @Test
    void swaggerUiLoads() {
        WebDriver driver = new HtmlUnitDriver(true);

        driver.get("http://localhost:" + port + "/swagger-ui/index.html");

        String source = driver.getPageSource().toLowerCase();
        assertTrue(source.contains("swagger"), "Swagger UI should be visible");
        driver.quit();
    }
}
