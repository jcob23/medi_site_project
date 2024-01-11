package pl.medisite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MediSiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediSiteApplication.class, args);
    }
}
