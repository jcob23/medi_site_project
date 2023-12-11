package pl.medisite.integration;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginControllerIT extends AbstractIT{

    @LocalServerPort
    private int port;

    private final TestRestTemplate testRestTemplate;

    @Test
    void applicationWorksCorrectly(){
        String url = "http://localhost:" + port + "/medisite/login";

        String page = this.testRestTemplate.getForObject(url, String.class);

        Assertions.assertThat(page).contains("<h3>Logowanie</h3>");
    }
}
