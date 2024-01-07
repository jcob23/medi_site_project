package pl.medisite.controller.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorRestController {

    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private PatientService patientService;

    @GetMapping("/appointments/{email}")
    public Set<AppointmentDTO> getAppointments(@PathVariable @Email String email) {
        return appointmentService.getDoctorAppointments(email);
    }

    @PostMapping("/add_appointment/{doctorEmail}")
    public NewAppointmentDTO addAppointment(
            @Valid @RequestBody NewAppointmentDTO newAppointmentDTO,
            @PathVariable String doctorEmail
    ) {
        appointmentService.createSingleAppointment(newAppointmentDTO, doctorEmail);
        return newAppointmentDTO;
    }

    @GetMapping("/patients/email")
    public Set<PersonDTO> getPatients(@PathVariable String email) {
        return doctorService.getPatients(email);
    }

    @GetMapping("/patient_diseases/{patientEmail}")
    public Set<DiseaseEntity> getDiseasesList(@PathVariable @Email String patientEmail) {
        return patientService.getDiseases(patientEmail);
    }

    @PatchMapping("/update_note/{appointmentId}")
    public ResponseEntity<?> updateNote(
            @PathVariable("appointmentId") Integer appointmentId,
            @RequestBody() String note
    ) {
        appointmentService.updateAppointment(appointmentId, note);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable("appointmentId") Integer appointmentId
    ) throws BadRequestException {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }


}
