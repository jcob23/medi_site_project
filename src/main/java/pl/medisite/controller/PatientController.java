package pl.medisite.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;
import pl.medisite.util.SecurityHelper;

import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/patient")
@AllArgsConstructor
@Slf4j
public class PatientController {

    private PatientService patientService;
    private AppointmentService appointmentService;
    private UserService userService;
    private DoctorService doctorService;
    private SecurityHelper securityHelper;

    @GetMapping()
    public String showPatientPage() {
        return "patient_profile";
    }

    @GetMapping("/doctors")
    public String showPatientDoctorsList(Model model) {
        List<PersonDTO.DoctorDTO> doctorsInformation = userService.getDoctorsInformation();
        model.addAttribute("personsData", doctorsInformation);
        return "patient_doctor_list";
    }

    @GetMapping("/diseases/{email}")
    public String showPatientDiseases(@PathVariable String email,
                                      Model model,
                                      Authentication authentication
    ) throws AccessDeniedException {
        securityHelper.checkAuthority(email, authentication);
        Set<DiseaseEntity> diseases = patientService.getDiseases(email);
        model.addAttribute("patientDiseases", diseases);
        return "patient_diseases";
    }

    @GetMapping("/appointments/{email}")
    public String showPatientAppointments(@PathVariable String email,
                                          Model model,
                                          Authentication authentication
    ) throws AccessDeniedException {
        securityHelper.checkAuthority(email, authentication);
        Set<AppointmentEntity> appointments = appointmentService.getPatientAppointments(email);
        model.addAttribute("patientAppointments", appointments);
        return "patient_appointments";
    }

    @GetMapping("/appointments_future/{email}")
    public String showPatientFutureAppointments(@PathVariable String email,
                                                Model model,
                                                Authentication authentication
    ) throws AccessDeniedException {
        securityHelper.checkAuthority(email, authentication);
        Set<AppointmentEntity> appointments = appointmentService.getPatientFutureAppointments(email);
        model.addAttribute("patientAppointments", appointments);
        return "patient_appointments";
    }

    @GetMapping("/appointments_past/{email}")
    public String showPatientPastAppointments(@PathVariable String email,
                                              Model model,
                                              Authentication authentication
    ) throws AccessDeniedException {
        securityHelper.checkAuthority(email, authentication);
        Set<AppointmentEntity> appointments = appointmentService.getPatientPastAppointments(email);
        model.addAttribute("patientAppointments", appointments);
        return "patient_appointments";
    }

    @GetMapping("/book_appointment")
    public String showBookAppointment(
            @RequestParam(name = "email") String email,
            Model model){
        DoctorEntity doctorEntity = doctorService.findByEmail(email);
        Set<AppointmentDTO> appointments = appointmentService.getDoctorFutureFreeAppointments(email);
        model.addAttribute("doctor",doctorEntity);
        model.addAttribute("appointments", appointments);
        return "patient_book_appointment";
    }
    @PutMapping("/book_appointment")
    public String bookAppointment(
            @RequestParam(name = "userEmail") String userEmail,
            @RequestParam(name = "appointmentId") Integer appointmentId,
            @RequestParam(name = "email") String email) {
        appointmentService.bookAppointment(appointmentId, userEmail);
        return "redirect:/patient/book_appointment?email=" + email;
    }

    @DeleteMapping("/delete_appointment")
    public String deleteAppointment(
            RedirectAttributes redirectAttributes,
            @RequestParam("email") String email,
            @RequestParam("appointmentId") Integer appointmentId
    ) throws BadRequestException {
        appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addAttribute("email", email);
        return "redirect:/patient/appointments/{email}";
    }

    @DeleteMapping()
    public String deletePatient(@RequestParam(name = "email") String email, Model model) {
        patientService.deletePatient(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }


}
