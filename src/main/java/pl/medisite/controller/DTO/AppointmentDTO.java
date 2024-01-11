package pl.medisite.controller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

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

