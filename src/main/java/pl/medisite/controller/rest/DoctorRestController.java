package pl.medisite.controller.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentDTO;
import pl.medisite.controller.DTO.Note;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DiseaseService;
import pl.medisite.service.DoctorService;

import java.util.AbstractMap;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/doctor")
public class DoctorRestController {

    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private DiseaseService diseaseService;


    @GetMapping("/appointments/{email}")
    public List<AppointmentDTO> getAppointments(@PathVariable @Email String email) {
        return appointmentService.getDoctorAppointments(email);
    }

    @GetMapping("/appointments_pageable/{email}")
    public List<AppointmentDTO> getAppointments(
            @PathVariable @Email String email,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer noOfElements) {
        PageRequest pageable = PageRequest.of(page - 1, noOfElements, Sort.by(Sort.Direction.ASC, "appointmentStart"));
        AbstractMap.SimpleEntry<Integer, List<AppointmentDTO>> doctorAppointments = appointmentService.getDoctorAppointments(email, null, pageable);
        return doctorAppointments.getValue();
    }

    @PostMapping("/add_appointment/{doctorEmail}")
    public NewAppointmentDTO addAppointment(
            @Valid @RequestBody NewAppointmentDTO newAppointmentDTO,
            @PathVariable String doctorEmail
    ) throws BindException {
        appointmentService.createSingleAppointment(newAppointmentDTO, doctorEmail);
        return newAppointmentDTO;
    }

    @GetMapping("/patients/email")
    public Set<PersonDTO> getPatients(@PathVariable String email) {
        return doctorService.getPatients(email);
    }

    @GetMapping("/patient_diseases/{patientEmail}")
    public Set<DiseaseEntity> getDiseasesList(@PathVariable @Email String patientEmail) {
        return diseaseService.getDiseases(patientEmail);
    }

    @GetMapping("/patient_appointments/{doctorEmail}/{patientEmail}")
    public Set<AppointmentDTO> getPatientAppointmentsForDoctor(
            @PathVariable @Email String patientEmail,
            @PathVariable @Email String doctorEmail) {
        return appointmentService.getPatientFutureAppointmentsForDoctor(patientEmail, doctorEmail);
    }

    @PatchMapping("/update_note/{appointmentId}")
    public ResponseEntity<?> updateNote(
            @PathVariable("appointmentId") Integer appointmentId,
            @RequestBody() Note note
    ) {
        appointmentService.updateAppointment(appointmentId, note);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable("appointmentId") Integer appointmentId
    ) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.noContent().build();
    }


}
