package pl.medisite.controller.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Getter
@Setter
public class NewAppointmentsDTO extends NewAppointmentDTO {

    @FutureOrPresent
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String visitTime;

    private String breakTime;

    public NewAppointmentsDTO() {
    }

    public NewAppointmentsDTO(
            @FutureOrPresent LocalDate date_input,
            String hours_input1,
            String hours_input2,
            LocalDate range_date_input,
            String visitTime,
            String breakTime
    ) {
        super(date_input, hours_input1, hours_input2);
        this.endDate = range_date_input;
        this.visitTime = visitTime;
        this.breakTime = breakTime;
    }
}
