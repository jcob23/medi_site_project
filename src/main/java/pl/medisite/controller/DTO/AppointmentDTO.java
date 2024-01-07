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


}

