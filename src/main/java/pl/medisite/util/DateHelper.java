package pl.medisite.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class DateHelper {
    public ZonedDateTime createZonedDateTime(String date, String time){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Parsowanie daty i godziny
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
      return ZonedDateTime.of(localDate,localTime, ZoneId.of("Europe/Warsaw"));
    }

    public Duration parseDuration(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return Duration.ofHours(localTime.getHour()).plusMinutes(localTime.getMinute());
    }
}
