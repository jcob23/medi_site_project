package pl.medisite.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class DateTimeHelper {


    public Duration parseDuration(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return Duration.ofHours(localTime.getHour()).plusMinutes(localTime.getMinute());
    }

    public static String createDisplayTime(int hours, int minutes){
        String hoursString = (hours>=0 && hours<10) ? "0" + hours : "" + hours;
        String minsString = (minutes>=0 && minutes<10) ? "0" + minutes : "" + minutes;
        return hoursString + ":" + minsString;
    }
}
