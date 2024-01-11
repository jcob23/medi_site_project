package pl.medisite.infrastructure.database.mapper;

import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.util.DateTimeHelper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


public class AppointmentEntityMapper {
    public static AppointmentDTO mapAppointment(AppointmentEntity appointmentEntity) {
        String patientName = appointmentEntity.getPatient() != null ?
                appointmentEntity.getPatient().getName() + " " + appointmentEntity.getPatient().getSurname() : " ";

        String doctorName = appointmentEntity.getDoctor() != null ?
                appointmentEntity.getDoctor().getName() + " " + appointmentEntity.getDoctor().getSurname() : " ";

        String doctorSpecialization = appointmentEntity.getDoctor() != null ?
                appointmentEntity.getDoctor().getSpecialization() : " ";

        String date = appointmentEntity.getAppointmentStart().getYear() + "."
                + appointmentEntity.getAppointmentStart().getMonthValue() + "."
                + appointmentEntity.getAppointmentStart().getDayOfMonth();

        String time = getAppointmentTime(appointmentEntity);

        ZonedDateTime appointmentEnd = appointmentEntity.getAppointmentEnd();
        LocalDateTime localAppointmentEnd = LocalDateTime.of(
                appointmentEnd.getYear(),
                appointmentEnd.getMonth(),
                appointmentEnd.getDayOfMonth(),
                appointmentEnd.getHour(),
                appointmentEnd.getMinute(),
                appointmentEnd.getSecond());


        return new AppointmentDTO(
                appointmentEntity.getAppointmentId(),
                patientName,
                doctorName,
                doctorSpecialization,
                date,
                time,
                appointmentEntity.getNote(),
                localAppointmentEnd);
    }

    private static String getAppointmentTime(AppointmentEntity appointmentEntity) {

        String timeStart = DateTimeHelper.createDisplayTime(
                appointmentEntity.getAppointmentStart().withZoneSameInstant(ZoneId.of("Europe/Warsaw")).getHour(),
                appointmentEntity.getAppointmentStart().getMinute());

        String timeEnd = DateTimeHelper.createDisplayTime(
                appointmentEntity.getAppointmentEnd().withZoneSameInstant(ZoneId.of("Europe/Warsaw")).getHour(),
                appointmentEntity.getAppointmentEnd().getMinute());
        return timeStart + " - " + timeEnd;
    }
}
