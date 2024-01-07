package pl.medisite.controller.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
public class NewAppointmentDTO {

    @FutureOrPresent
    @DateTimeFormat(pattern ="yyyy-MM-dd")
    @Schema(name = "Data wizyty", example = "2024-01-25")
    private LocalDate appointmentDate;

    @DateTimeFormat(pattern ="HH:mm:ss")
    @Schema(name = "Godzina rozpoczęcia wizyty", example = "08:00")
    private String appointmentTimeStart;

    @DateTimeFormat(pattern ="HH:mm:ss")
    @Schema(name = "Godzina zakończenia wizyty", example = "09:00")
    private String appointmentTimeEnd;
    public NewAppointmentDTO() {
    }
    public NewAppointmentDTO(LocalDate appointmentDate, String appointmentTimeStart, String appointmentTimeEnd) {
        this.appointmentDate = appointmentDate;
        this.appointmentTimeStart = appointmentTimeStart;
        this.appointmentTimeEnd = appointmentTimeEnd;
    }
}
