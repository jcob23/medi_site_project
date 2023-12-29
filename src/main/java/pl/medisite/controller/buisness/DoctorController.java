package pl.medisite.controller.buisness;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
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
        Set<AppointmentEntity> appointments = doctorService.getAppointments(email);
        model.addAttribute("doctorAppointmentDTO", new DoctorAppointmentDTO());
        model.addAttribute("appointments",appointments);
        return "doctor_appointments";
    }

    @PostMapping("/add_appointment")
    public String addAppointment(
        @ModelAttribute("doctorAppointmentDTO") DoctorAppointmentDTO doctorAppointmentDTO,
        HttpSession session
    ) throws ParseException {
        String email = (String) session.getAttribute("userEmail");
        appointmentService.createNewAppointment(doctorAppointmentDTO, email);

        return "redirect:/doctor/appointments";
    }


    @GetMapping("/edit_note")
    public String showEditNote(
            @RequestParam("email") String email,
            @RequestParam("appointmentId") Integer appointmentId,
            Model model
    ) {
        AppointmentEntity appointment = appointmentService.getById(appointmentId);
        model.addAttribute("appointment",appointment);
        return "doctor_appointments";
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
