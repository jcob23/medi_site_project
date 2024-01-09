package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;
import pl.medisite.util.SecurityHelper;

import java.util.Set;


@Controller
@RequestMapping("/patient")
@AllArgsConstructor
@Slf4j
public class PatientController {

    private UserService userService;
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;

    private SecurityHelper securityHelper;
    private HttpSession session;

    @GetMapping()
    public String showPatientPage(Model model) {
        String email = (String) session.getAttribute("userEmail");
        PersonDTO patient = patientService.getPatient(email);
        model.addAttribute("patient", patient);
        return "profile";
    }

    @GetMapping("/doctors")
    public String showDoctorsList(Model model) {
        Set<PersonDTO.DoctorDTO> doctorsInformation = userService.getAllDoctors();
        model.addAttribute("personsData", doctorsInformation);
        return "patient_doctor_list";
    }

    @GetMapping("/appointments")
    public String showPatientAppointments(Model model, @RequestParam(name = "filter", required = false) String filter
    ) throws AccessDeniedException {
        String email = (String) session.getAttribute("userEmail");
        Set<AppointmentDTO> appointments = appointmentService.getPatientAppointments(email, filter);
        model.addAttribute("patientAppointments", appointments);
        return "patient_appointments";
    }

    @GetMapping("/book_appointment")
    public String showBookAppointment(@RequestParam(name = "email") @Email String email, Model model) {
        PersonDTO.DoctorDTO doctor = doctorService.getDoctor(email);
        Set<AppointmentDTO> appointments = appointmentService.getDoctorFutureFreeAppointments(email);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "patient_book_appointment";
    }

    @PutMapping("/book_appointment")
    public String bookAppointment(
            @RequestParam(name = "appointmentId") Integer appointmentId,
            @RequestParam(name = "email") @Email String email) {
        String userEmail = (String) session.getAttribute("userEmail");
        appointmentService.bookAppointment(appointmentId, userEmail);
        return "redirect:/patient/book_appointment?email=" + email;
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public String deleteAppointment(
            @PathVariable("appointmentId") Integer appointmentId
    ) throws BadRequestException {
        appointmentService.deleteAppointment(appointmentId);
        return "redirect:/patient/appointments";
    }

    @PutMapping("/update")
    public String updateInformation(@Valid @ModelAttribute("patient") PersonDTO user, Model model){
        patientService.updatePatient(user);
        model.addAttribute("update", true);
        return "profile";
    }


    @DeleteMapping("/{email}")
    public String deletePatient(@PathVariable(name = "email") @Email String email, Model model, Authentication authentication) {
        securityHelper.compareUserEmailWithRequestEmail((User) authentication.getPrincipal(),email);
        patientService.deletePatient(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }


}
