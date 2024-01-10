package pl.medisite;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;


@SpringBootApplication
@EnableScheduling
public class MediSiteApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediSiteApplication.class, args);
    }
}
