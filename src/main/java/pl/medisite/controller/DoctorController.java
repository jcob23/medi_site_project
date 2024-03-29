package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.*;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.util.Constants;
import pl.medisite.util.SecurityHelper;

import java.util.AbstractMap;
import java.util.List;
import java.util.Objects;
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

    private HttpSession session;

    @GetMapping()
    public String showDoctorProfile(Model model) {
        String email = (String) session.getAttribute("userEmail");
        PersonDTO.DoctorDTO doctor = doctorService.getDoctor(email);
        model.addAttribute("doctor", doctor);
        return "profile";
    }

    @GetMapping("/patients")
    public String showPatients(Model model) {
        String email = (String) session.getAttribute("userEmail");
        Set<PersonDTO> patients = doctorService.getPatients(email);
        model.addAttribute("patientsList", patients);
        return "doctor_patients";
    }

    @GetMapping("/appointments")
    public String showDoctorAppointments(
            Model model,
            @RequestParam(name = "dateFilter", required = false) String dateFilter,
            @RequestParam(defaultValue = "1") Integer page
    ) {
        PageRequest pageable = PageRequest.of(page - 1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC, "appointmentStart"));
        String email = (String) session.getAttribute("userEmail");
        AbstractMap.SimpleEntry<Integer, List<AppointmentDTO>> appointments = appointmentService.getDoctorAppointments(email, dateFilter, pageable);

        model.addAttribute("newAppointmentDTO", new NewAppointmentDTO());
        model.addAttribute("newAppointmentsDTO", new NewAppointmentsDTO());
        model.addAttribute("appointments", appointments.getValue());
        model.addAttribute("numberOfPages", appointments.getKey());
        if( !Objects.isNull(dateFilter) ) {
            model.addAttribute("filter", dateFilter);
        }
        return "doctor_appointments";
    }

    @GetMapping("/patient_appointments/{patientEmail}")
    public String showPatientAppointments(
            @PathVariable("patientEmail") @Email String patientEmail,
            Model model) {
        String doctorEmail = (String) session.getAttribute("userEmail");
        PersonDTO patient = patientService.getPatient(patientEmail);
        Set<AppointmentDTO> appointments = appointmentService.getPatientFutureAppointmentsForDoctor(patientEmail, doctorEmail);
        model.addAttribute("patient", patient);
        model.addAttribute("patientAppointments", appointments);
        return "doctor_patient_appointment";
    }

    @PostMapping("/add_single_appointment")
    public String addAppointment(
            @Valid @ModelAttribute("newAppointmentDTO") NewAppointmentDTO newAppointmentDTO,
            RedirectAttributes redirectAttributes) throws BindException {
        String email = (String) session.getAttribute("userEmail");
        appointmentService.createSingleAppointment(newAppointmentDTO, email);
        redirectAttributes.addFlashAttribute("added", true);
        return "redirect:/doctor/appointments";
    }

    @PostMapping("/add_multiple_appointments")
    public String addAppointments(
            @Valid @ModelAttribute("newAppointmentDTO") NewAppointmentsDTO newAppointmentsDTO,
            RedirectAttributes redirectAttributes) throws BindException {
        String email = (String) session.getAttribute("userEmail");
        appointmentService.createMultipleAppointments(newAppointmentsDTO, email);
        redirectAttributes.addFlashAttribute("added2", true);
        return "redirect:/doctor/appointments";
    }

    @GetMapping("/appointment_details/{appointmentId}")
    public String showAppointmentDetails(@PathVariable("appointmentId") Integer appointmentId, Model model) {
        securityHelper.checkDoctorAccessToAppointmentInformation(
                appointmentId, (String) session.getAttribute("userEmail"));
        AppointmentDTO appointment = appointmentService.getAppointment(appointmentId);
        model.addAttribute("appointment", appointment);
        model.addAttribute("note", new Note(appointment.getNote()));
        return "doctor_details_appointment";
    }

    @PutMapping("/update_note/{appointmentId}")
    public String updateNote(
            @PathVariable("appointmentId") Integer appointmentId,
            @ModelAttribute("note") Note note
    ) {
        securityHelper.checkDoctorAccessToAppointmentInformation(
                appointmentId, (String) session.getAttribute("userEmail"));
        appointmentService.updateAppointment(appointmentId, note);
        return "redirect:/doctor/appointment_details/" + appointmentId;
    }

    @PutMapping("/update")
    public String updateInformation(@Valid @ModelAttribute("doctor") PersonDTO.DoctorDTO user, Model model) {
        doctorService.updateDoctor(user);
        model.addAttribute("update", true);
        return "profile";
    }

    @DeleteMapping("/delete_appointment/{appointmentId}")
    public String deleteAppointment(
            @PathVariable("appointmentId") Integer appointmentId,
            RedirectAttributes redirectAttributes
    ) {
        appointmentService.deleteAppointment(appointmentId);
        redirectAttributes.addFlashAttribute("deleted", true);
        return "redirect:/doctor/appointments";
    }

    @DeleteMapping("/delete/{email}")
    public String deleteDoctor(@PathVariable(name = "email") @Email String email, Authentication authentication, Model model) {
        securityHelper.compareUserEmailWithRequestEmail((User) authentication.getPrincipal(), email);
        doctorService.deleteDoctor(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }


}
