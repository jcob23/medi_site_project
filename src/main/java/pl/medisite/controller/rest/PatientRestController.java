package pl.medisite.controller.rest;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DiseaseService;
import pl.medisite.service.UserService;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/patient")
public class PatientRestController {

    private UserService userService;
    private AppointmentService appointmentService;
    private DiseaseService diseaseService;

    @GetMapping("/doctors")
    public Set<PersonDTO.DoctorDTO> getDoctorsList() {
        return userService.getAllDoctors();
    }

    @GetMapping("/diseases/{email}")
    public Set<DiseaseEntity> getDiseasesList(@PathVariable String email) {
        return diseaseService.getDiseases(email);
    }

    @GetMapping("/appointments/{email}")
    public Set<AppointmentDTO> getAppointments(@PathVariable String email) {
        return appointmentService.getPatientAppointments(email, null);
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public ResponseEntity<?> deleteAppointment
            (@PathVariable("appointmentId") Integer appointmentId) throws BadRequestException {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/book_appointment/{appointmentId}")
    public ResponseEntity<?> bookAppointment(
            @RequestBody(
                    description = "Email address",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(value = "user@example.com")
                    )
            ) String userEmail,
            @PathVariable(name = "appointmentId") Integer appointmentId
    ) {
        appointmentService.bookAppointment(appointmentId, userEmail);
        return ResponseEntity.ok().build();
    }


}
