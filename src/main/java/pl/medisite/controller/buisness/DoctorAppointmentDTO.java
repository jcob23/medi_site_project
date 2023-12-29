package pl.medisite.controller.buisness;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorAppointmentDTO {

    private String type;
    private  String date_input;
    private  String range_date_input;
    private  String hours_input1;
    private  String hours_input2;
    private  String visit_time_input;
    private  String break_input;
}
