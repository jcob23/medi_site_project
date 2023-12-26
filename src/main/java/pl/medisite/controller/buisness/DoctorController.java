package pl.medisite.controller.buisness;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;
import pl.medisite.util.SecurityHelper;

import java.util.Set;

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor
public class DoctorController {

    private DoctorService doctorService;
    private AppointmentService appointmentService;

    @GetMapping()
    public String showDoctorPage() {
        return "doctor_profile";
    }

    @GetMapping("/calendar")
    public String showDoctorCalendar(Model model,
                                     HttpSession session
    ) {
        String email = (String) session.getAttribute("userEmail");
        Set<AppointmentEntity> appointments = doctorService.getAppointments(email);
        model.addAttribute("appointments",appointments);
        return "doctor_appointments";
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
