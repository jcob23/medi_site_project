package pl.medisite.controller.rest;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DiseaseService;
import pl.medisite.service.UserService;
import pl.medisite.util.Constants;

import java.util.AbstractMap;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api/patient")
public class PatientRestController {

    private UserService userService;
    private AppointmentService appointmentService;
    private DiseaseService diseaseService;

    @GetMapping("/doctors")
    public List<PersonDTO.DoctorDTO> getDoctorsList() {
        return userService.getAllDoctors();
    }

    @GetMapping("/diseases/{email}")
    public Set<DiseaseEntity> getDiseasesList(@PathVariable String email) {
        return diseaseService.getDiseases(email);
    }

    @GetMapping("/appointments/{email}")
    public List<AppointmentDTO> getAppointments(@PathVariable String email) {
        return appointmentService.getPatientAppointments(email);
    }
    @GetMapping("/appointments_pageable/{email}")
    public List<AppointmentDTO> getAppointments(@PathVariable String email,@RequestParam(defaultValue = "1") Integer page) {
        PageRequest pageable = PageRequest.of(page-1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC,"appointmentStart"));
        AbstractMap.SimpleEntry<Integer, List<AppointmentDTO>> patientAppointments = appointmentService.getPatientAppointments(email, null, pageable);
        return patientAppointments.getValue();
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
