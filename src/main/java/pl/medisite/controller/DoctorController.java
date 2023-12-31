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
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.DoctorAppointmentDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;

import java.text.ParseException;
import java.util.Set;

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor
@Slf4j
public class DoctorController {

    private DoctorService doctorService;
    private AppointmentService appointmentService;

    @GetMapping()
    public String showDoctorPage() {
        return "doctor_profile";
    }

    @GetMapping("/appointments")
    public String showDoctorCalendar(Model model,
                                     HttpSession session
    ) {
        String email = (String) session.getAttribute("userEmail");
        Set<AppointmentDTO> appointments = doctorService.getAppointmentsDTO(email);
        model.addAttribute("doctorAppointmentDTO", new DoctorAppointmentDTO());
        model.addAttribute("appointments", appointments);
        return "doctor_appointments";
    }
    @PostMapping("/add_appointment")
    public String addAppointment(
            @Valid @ModelAttribute("doctorAppointmentDTO") DoctorAppointmentDTO doctorAppointmentDTO,
            HttpSession session
    ) throws ParseException {
        String email = (String) session.getAttribute("userEmail");
        appointmentService.createNewAppointment(doctorAppointmentDTO, email);
        return "redirect:/doctor/appointments";
    }

    @GetMapping("/details_appointment")
    public String showAppointmentDetails(
            @RequestParam("appointmentId") Integer appointmentId,
            Model model
    ) {
        AppointmentDTO appointment = appointmentService.getAppointment(appointmentId);
        model.addAttribute("appointment", appointment);
        return "doctor_details_appointment";
    }
    @GetMapping("/patient_diseases")
    public String getPatientDiseases(
            @RequestParam("appointmentId") Integer appointmentId,
            Model model
    ){
        Set<DiseaseEntity> diseases =  appointmentService.getPatientDiseases(appointmentId);
        model.addAttribute("patientDiseases", diseases);
        return "patient_diseases";
    }

    @PutMapping("/update_note")
    public String updateNote(
            @RequestParam("appointmentId") Integer appointmentId,
            @RequestParam("note") String note,
            RedirectAttributes redirectAttributes
    ) {
        appointmentService.updateAppointment(appointmentId,note);
        redirectAttributes.addAttribute("appointmentId",appointmentId);
        return "redirect:/doctor/details_appointment";
    }

    @DeleteMapping("/delete_appointment")
    public String deleteAppointment(
            @RequestParam("appointmentId") Integer appointmentId
    ) throws BadRequestException {
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
