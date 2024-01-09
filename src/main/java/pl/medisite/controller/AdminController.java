package pl.medisite.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.DTO.DoctorDTO;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;

import java.util.Set;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private UserService userService;
    private PatientService patientService;
    private DoctorService doctorService;

    @GetMapping("/edit_patient/{email}")
    public String showEditPatient(@PathVariable("email") String email, Model model) {
        PersonDTO patient = patientService.getPatient(email);
        model.addAttribute("user", patient);
        return "admin_edit";
    }

    @PutMapping("/edit_patient")
    public String editPatient(@ModelAttribute("patient") PersonDTO patient) {
        patientService.updatePatient(patient);
        return "redirect:/admin/patients";
    }

    @GetMapping("/edit_doctor/{email}")
    public String showEditDoctor(@PathVariable("email") String email, Model model) {
        PersonDTO.DoctorDTO doctorEntity = doctorService.getDoctor(email);
        model.addAttribute("user", doctorEntity);
        return "admin_edit";
    }

    @PutMapping("/edit_doctor")
    public String editDoctor(@ModelAttribute("user") PersonDTO.DoctorDTO doctorEntity) {
        doctorService.updateDoctor(doctorEntity);
        return "redirect:/admin/doctors";
    }

    @DeleteMapping("/delete_patient/{email}")
    public String deletePatient(@PathVariable String email) {
        patientService.deletePatient(email);
        return "redirect:/admin/patients";
    }

    @DeleteMapping("/delete_doctor/{email}")
    public String deleteDoctor(@PathVariable String email) {
        doctorService.deleteDoctor(email);
        return "redirect:/admin/doctors";
    }

    @GetMapping("/add")
    public String adminAddPage(Model model) {
        model.addAttribute("doctorDTO", new DoctorDTO());
        return "admin_add";
    }

    @PostMapping("/add")
    public String adminAddDoctor(@Valid @ModelAttribute("doctorDTO") DoctorDTO doctorDTO) {
        doctorService.saveDoctor(doctorDTO);
        return "redirect:/admin/add";
    }

    @GetMapping("/patients")
    public String adminPatientsPage(Model model, Authentication authentication) {
        Set<PersonDTO> users = userService.getAllPatients();
        model.addAttribute("patientView", true);
        model.addAttribute("personsData", users);
        return "admin_list";
    }

    @GetMapping("/doctors")
    public String adminDoctorsPage(Model model, Authentication authentication) {
        Set<PersonDTO.DoctorDTO> users = userService.getAllDoctors();
        model.addAttribute("doctorView", true);
        model.addAttribute("personsData", users);
        return "admin_list";
    }


    @GetMapping("/users")
    public String adminUsersPage(Model model, Authentication authentication) {
        Set<PersonDTO> users = userService.getAllUsersInformation();
        model.addAttribute("personsData", users);
        return "admin_list";
    }
}
