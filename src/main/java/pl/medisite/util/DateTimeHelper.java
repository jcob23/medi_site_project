package pl.medisite.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import pl.medisite.exception.AppointmentOverlapException;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Slf4j
@Component
public class DateTimeHelper {
    public static String createDisplayTime(int hours, int minutes) {
        String hoursString = (hours >= 0 && hours < 10) ? "0" + hours : "" + hours;
        String minsString = (minutes >= 0 && minutes < 10) ? "0" + minutes : "" + minutes;
        return hoursString + ":" + minsString;
    }

    public Duration parseDuration(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(time, formatter);
        return Duration.ofHours(localTime.getHour()).plusMinutes(localTime.getMinute());
    }

    public void checkIfDateIsAvailable(AppointmentEntity newAppointment, Set<AppointmentEntity> doctorAppointments) {
        for(AppointmentEntity appointment : doctorAppointments) {
            isOverlap(newAppointment, appointment);
        }
    }

    private void isOverlap(AppointmentEntity newAppointment, AppointmentEntity appointment) {
        if( newAppointment.getAppointmentStart().toLocalDate().equals(appointment.getAppointmentStart().toLocalDate()) ) {
            LocalTime newTimeStart = newAppointment.getAppointmentStart().toLocalTime();
            LocalTime newTimeEnd = newAppointment.getAppointmentEnd().toLocalTime();
            LocalTime existingTimeStart = appointment.getAppointmentStart().toLocalTime();
            LocalTime existingTimeEnd = appointment.getAppointmentEnd().toLocalTime();

            if( newTimeStart.isAfter(existingTimeStart) && newTimeEnd.isBefore(existingTimeEnd) || newTimeStart.equals(existingTimeStart) && newTimeEnd.equals(existingTimeEnd) ) {
                throw new AppointmentOverlapException("Nowa wizyta: " + newAppointment + "miałaby się odbyć w czasie istniejącej już wizyty: " + appointment);
            } else if( newTimeStart.isBefore(existingTimeEnd) ) {
                throw new AppointmentOverlapException("Początek nowej wizyty: " + newAppointment + " pokrywa się z wizytą: " + appointment);
            } else if( newTimeStart.isAfter(existingTimeStart) ) {
                throw new AppointmentOverlapException("Koniec nowej wizyty: " + newAppointment + " pokrywa się z wizytą: " + appointment);
            }

        }
    }

    public void checkIfAppointmentTimeIsValid(LocalTime timeStart, LocalTime timeEnd) throws BindException {
        if( timeEnd.isBefore(timeStart) ) {
            FieldError fieldError = getFieldError(timeEnd);

            BindException bindException = new BindException(fieldError, "newAppointmentDTO");
            bindException.addError(fieldError);

            throw bindException;
        }
    }

    private static FieldError getFieldError(LocalTime timeEnd) {
        String errorCode = "appointment.invalidTimeRange";

        FieldError fieldError = new FieldError(
                "newAppointment", //ObjectName
                "appointmentTimeEnd", // Field
                timeEnd, // Rejected value
                false, // Binding failure
                new String[]{errorCode}, // Codes
                null, // Arguments
                "" // DefaultMessage
        );
        return fieldError;
    }


}
