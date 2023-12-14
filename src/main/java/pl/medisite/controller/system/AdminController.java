package pl.medisite.controller.system;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.buisness.DoctorDTO;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.entity.PersonInformation;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {

    private UserService userService;
    private PatientService patientService;
    private DoctorService doctorService;

    @GetMapping("/edit_patient/{email}")
    public String showEditUser(@PathVariable("email") String email, Model model) {
        PatientEntity patientEntity = patientService.findByEmail(email);
        model.addAttribute("user", patientEntity);
        return "admin_edit";
    }

    @PutMapping("/edit_patient")
    public String editDoctor(@ModelAttribute("user") DoctorEntity doctorEntity) {
        doctorService.updateDoctor(doctorEntity);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/edit_doctor/{email}")
    public String showEditDoctor(@PathVariable("email") String email, Model model) {
        DoctorEntity doctorEntity = doctorService.findByEmail(email);
        model.addAttribute("user", doctorEntity);
        return "admin_edit";
    }

    @PutMapping("/edit_doctor")
    public String editDoctor(@ModelAttribute("user") PatientEntity patientEntity) {
        patientService.updatePatient(patientEntity);
        return "redirect:/admin/plebs";
    }

    @DeleteMapping("/delete_patient/{email}")
    public String deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
        return "redirect:/admin/plebs";
    }

    @DeleteMapping("/delete_doctor/{email}")
    public String deleteDoctor(@PathVariable String email) {
        doctorService.deleteDoctor(email);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/add")
    public String adminAddPage(Model model, Authentication authentication) {
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("doctorDTO", new DoctorDTO());
        return "admin_add";
    }

    @PostMapping("/add")
    public String adminAddDoctor(
            @Valid @ModelAttribute("doctorDTO") DoctorDTO doctorDTO
    ) {
        doctorService.saveDoctor(doctorDTO);
        return "redirect:/admin/add";
    }

    @GetMapping("/plebs")
    public String adminPlebsPage(Model model, Authentication authentication) {
        List<PersonInformation> users = userService.getUsersEmails();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("personsData", users);
        return "admin_list";
    }

    @GetMapping("/doctors")
    public String adminDoctorsPage(Model model, Authentication authentication) {
        List<PersonInformation> users = userService.getDoctorsEmails();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("personsData", users);
        return "admin_list";
    }

    @GetMapping("/users")
    public String adminUsersPage(Model model, Authentication authentication) {
        List<PersonInformation> users = userService.getAllEmails();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("personsData", users);
        return "admin_list";
    }
}
