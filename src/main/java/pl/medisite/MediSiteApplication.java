package pl.medisite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.medisite.infrastructure.database.entity.DoctorEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class MediSiteApplication {
    public static void main(String[] args) throws ParseException {
        SpringApplication.run(MediSiteApplication.class, args);

    }


}
