package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.*;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.util.SecurityHelper;

import java.util.Set;

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor
@Slf4j
public class DoctorController {

    private DoctorService doctorService;
    private PatientService patientService;
    private AppointmentService appointmentService;

    private SecurityHelper securityHelper;

    @GetMapping()
    public String showDoctorProfile() {
        return "doctor_profile";
    }

    @GetMapping("/patients")
    public String showPatients(
            HttpSession session,
            Model model) {
        String email = (String) session.getAttribute("userEmail");
        Set<PersonDTO> patients = doctorService.getPatients(email);
        model.addAttribute("patientsList", patients);
        return "doctor_patients";
    }

    @GetMapping("/appointments")
    public String showDoctorAppointments(
            Model model,
            HttpSession session,
            @RequestParam(name = "dateFilter", required = false) String dateFilter
    ) {
        String email = (String) session.getAttribute("userEmail");
        Set<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(email, dateFilter);
        model.addAttribute("newAppointmentDTO", new NewAppointmentDTO());
        model.addAttribute("newAppointmentsDTO", new NewAppointmentsDTO());
        model.addAttribute("appointments", appointments);
        return "doctor_appointments";
    }

    @GetMapping("/patient_appointments/{patientEmail}")
    public String showPatientAppointments(
            @PathVariable("patientEmail") String patientEmail,
            HttpSession session,
            Model model) {
        String doctorEmail = (String) session.getAttribute("userEmail");
        PersonDTO patient = patientService.getPatient(patientEmail);
        Set<AppointmentDTO> appointments = appointmentService.getPatientFutureAppointmentsForDoctor(patientEmail,doctorEmail);
        model.addAttribute("patient", patient);
        model.addAttribute("patientAppointments", appointments);
        return "doctor_patient_appointment";
    }

    @PostMapping("/add_single_appointment")
    public String addAppointment(
            @Valid @ModelAttribute("newAppointmentDTO") NewAppointmentDTO newAppointmentDTO,
            HttpSession session
    ) {
        String email = (String) session.getAttribute("userEmail");
        appointmentService.createSingleAppointment(newAppointmentDTO, email);
        return "redirect:/doctor/appointments";
    }

    @PostMapping("/add_multiple_appointments")
    public String addAppointments(
            @Valid @ModelAttribute("newAppointmentDTO") NewAppointmentsDTO newAppointmentsDTO,
            HttpSession session
    ){
        String email = (String) session.getAttribute("userEmail");
        appointmentService.createMultipleAppointments(newAppointmentsDTO, email);
        return "redirect:/doctor/appointments";
    }

    @GetMapping("/appointment_details/{appointmentId}")
    public String showAppointmentDetails(
            @PathVariable("appointmentId") Integer appointmentId,
            HttpSession session,
            Model model
    ) {
        securityHelper.checkDoctorAccessToAppointmentInformation(
                appointmentId,(String) session.getAttribute("userEmail"));
        AppointmentDTO appointment = appointmentService.getAppointment(appointmentId);
        model.addAttribute("appointment", appointment);
        model.addAttribute("note", new Note(appointment.getNote()));
        return "doctor_details_appointment";
    }

    @PutMapping("/update_note/{appointmentId}")
    public String updateNote(
            @PathVariable("appointmentId") Integer appointmentId,
            @ModelAttribute("note") Note note,
            HttpSession session
    ) {
        securityHelper.checkDoctorAccessToAppointmentInformation(
                appointmentId,(String) session.getAttribute("userEmail"));
        appointmentService.updateAppointment(appointmentId, note);
        return "redirect:/doctor/appointment_details/" + appointmentId;
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public String deleteAppointment(@PathVariable("appointmentId") Integer appointmentId) throws BadRequestException {
        appointmentService.deleteAppointment(appointmentId);
        return "redirect:/doctor/appointments";
    }

    @DeleteMapping()
    public String deleteDoctor(Authentication authentication, Model model) {
        String email = ((UserDetails) authentication.getPrincipal()).getUsername();
        doctorService.deleteDoctor(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }


}
