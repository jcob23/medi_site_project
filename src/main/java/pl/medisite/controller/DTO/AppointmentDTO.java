package pl.medisite.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.util.DateTimeHelper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class AppointmentDTO {
    private int id;
    private String patientName;
    private String doctorName;
    private String doctorSpecialization;
    private String date;
    private String time;
    private String note;
    private LocalDateTime appointmentEnd;

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

        String timeStart = DateTimeHelper.createDisplayTime(
                appointmentEntity.getAppointmentStart().getHour(),
                appointmentEntity.getAppointmentStart().getMinute());

        String timeEnd = DateTimeHelper.createDisplayTime(
                appointmentEntity.getAppointmentEnd().getHour(),
                appointmentEntity.getAppointmentEnd().getMinute());
        String time = timeStart + " - " + timeEnd;

        ZonedDateTime appointmentEnd = appointmentEntity.getAppointmentEnd();
        LocalDateTime localAppointmentEnd = LocalDateTime.of(
                appointmentEnd.getYear(),
                appointmentEnd.getMonth(),
                appointmentEnd.getDayOfMonth(),
                appointmentEnd.getHour(),
                appointmentEnd.getMinute(),
                appointmentEnd.getSecond());


        return new AppointmentDTO(appointmentEntity.getId(),
                patientName,
                doctorName,
                doctorSpecialization,
                date,
                time,
                appointmentEntity.getNote(),
                localAppointmentEnd);
    }
}
