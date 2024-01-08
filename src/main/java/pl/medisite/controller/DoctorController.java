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
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
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
    public String showDoctorPage() {
        return "doctor_profile";
    }

    @GetMapping("/appointments")
    public String showDoctorCalendar(Model model,
                                     HttpSession session
    ) {
        String email = (String) session.getAttribute("userEmail");
        Set<AppointmentDTO> appointments = appointmentService.getDoctorAppointments(email);
        model.addAttribute("newAppointmentDTO", new NewAppointmentDTO());
        model.addAttribute("newAppointmentsDTO", new NewAppointmentsDTO());
        model.addAttribute("appointments", appointments);
        return "doctor_appointments";
    }

    @GetMapping("/appointment_details/{appointmentId}")
    public String showAppointmentDetails(
            @PathVariable("appointmentId") Integer appointmentId,
            Model model
    ) {
        AppointmentDTO appointment = appointmentService.getAppointment(appointmentId);
        model.addAttribute("appointment", appointment);
        return "doctor_details_appointment";
    }

    @GetMapping("/patients")
    public String showPatients(HttpSession session, Model model) {
        String email = (String) session.getAttribute("userEmail");
        Set<PersonDTO> patients = doctorService.getPatients(email);
        model.addAttribute("patientsList", patients);
        return "doctor_patients";
    }

    @GetMapping("/patient_appointments")
    public String showPatientAppointments(
            @RequestParam("patientEmail") String patientEmail,
            HttpSession session,
            Model model) {
        String doctorEmail = (String) session.getAttribute("userEmail");
        PersonDTO patient = patientService.getPatient(patientEmail);
        Set<AppointmentDTO> appointments = doctorService.getPatientsAppointmentForDoctor(patientEmail,doctorEmail);

        model.addAttribute("patient", patient);
        model.addAttribute("patientAppointments", appointments);
        return "doctor_patient_appointment";
    }


    @GetMapping("/patient_diseases_email/{patientEmail}")
    public String showPatientDiseasesByEmail(
            @PathVariable("patientEmail") String patientEmail,
            HttpSession session,
            Model model
    ) {
        String doctorEmail = (String) session.getAttribute("userEmail");
        Set<PersonDTO> patientEmails = doctorService.getPatients(doctorEmail);
        securityHelper.checkDoctorAccessToPatientInformation(patientEmail,patientEmails);
        Set<DiseaseEntity> diseases = patientService.getDiseases(patientEmail);
        model.addAttribute("patientDiseasesList", diseases);
        model.addAttribute("diseaseDTO", new DiseaseDTO());
        return "patient_diseases";
    }

    @GetMapping("/edit_disease")
    public String showEditPatientDiseases(
            @RequestParam("disease") DiseaseEntity diseaseEntity,
            Model model
    ) {
        model.addAttribute("diseaseEntity", diseaseEntity);
        return "doctor_edit_disease";
    }

    @PostMapping("/add_single_appointment")
    public String addAppointment(
            @Valid @ModelAttribute("newAppointmentDTO") NewAppointmentDTO newAppointmentDTO,
            HttpSession session
    ) {
        String email = (String) session.getAttribute("userEmail");
        log.info("getAppointmentDate: " + newAppointmentDTO.getAppointmentDate());
        log.info("getAppointmentTimeStart: " + newAppointmentDTO.getAppointmentTimeStart());
        log.info("getAppointmentTimeEnd: " + newAppointmentDTO.getAppointmentTimeEnd());
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


    @PostMapping("/patient_disease")
    public String addDisease(
            @RequestParam("appointmentId") Integer appointmentId,
            @Valid @ModelAttribute("diseaseDTO") DiseaseDTO diseaseEntity,
            RedirectAttributes redirectAttributes) {
        doctorService.addDiseaseToPatientByAppointmentId(appointmentId, diseaseEntity);
        redirectAttributes.addAttribute("appointmentId", appointmentId);
        return "redirect:/doctor/patient_diseases";
    }

    @PutMapping("/update_note")
    public String updateNote(
            @RequestParam("appointmentId") Integer appointmentId,
            @RequestParam("note") String note,
            RedirectAttributes redirectAttributes
    ) {
        appointmentService.updateAppointment(appointmentId, note);
        redirectAttributes.addAttribute("appointmentId", appointmentId);
        return "redirect:/doctor/details_appointment";
    }

    @DeleteMapping("/delete_appointment")
    public String deleteAppointment(@RequestParam("appointmentId") Integer appointmentId) throws BadRequestException {
        appointmentService.deleteAppointment(appointmentId);
        return "redirect:/doctor/appointments";
    }

    @DeleteMapping()
    public String deleteDoctor(Authentication authentication, Model model) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String email = principal.getUsername();
        doctorService.deleteDoctor(email);
        model.addAttribute("deleted", true);
        SecurityContextHolder.getContext().setAuthentication(null);
        return "login";
    }


}
