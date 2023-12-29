package pl.medisite.controller.buisness;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAppointmentDTO {

    private String type;

    @FutureOrPresent
    private LocalDate date_input;

    @FutureOrPresent
    private LocalDate range_date_input;

    @FutureOrPresent
    private LocalTime hours_input1;

    @FutureOrPresent
    private LocalTime hours_input2;

    private String visit_time_input;

    private String break_input;
}
