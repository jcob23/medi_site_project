package pl.medisite.controller.system;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.buisness.DoctorDTO;
import pl.medisite.controller.buisness.PersonDTO;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.security.UserEntity;
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

    @GetMapping("/plebs")
    public String adminPlebsPage(Model model, Authentication authentication) {
        List<PersonDTO> users = userService.getUsersEmails();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("personsData", users);
        return "admin_list";
    }
    @GetMapping("/doctors")
    public String adminDoctorsPage(Model model, Authentication authentication) {
        List<PersonDTO> users = userService.getDoctorsEmails();
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("personsData", users);
        return "admin_list";
    }
    @GetMapping("/users")
    public String adminUsersPage(Model model, Authentication authentication) {
        List<PersonDTO> users = userService.getAllEmails();
        log.info("###" +  users.size());
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("userRole", userRole);
        model.addAttribute("personsData", users);
        return "admin_list";
    }
    @DeleteMapping("/delete/{email}")
    public String deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return "redirect:/admin_list";
    }
    @GetMapping("/edit_patient/{email}")
    public String showEditUser(@PathVariable String email,Model model){
        PatientEntity patientEntity = patientService.findByEmail(email);
        model.addAttribute("user",patientEntity);
        return "admin_edit";
    }

    @PutMapping("/edit_patient")
    public String editUser(@ModelAttribute("user") PatientEntity patientEntity){
        PatientEntity existingPatient = patientService.findByEmail(patientEntity.getLoginDetails().getEmail());
        existingPatient.setName(patientEntity.getName());
        existingPatient.setSurname(patientEntity.getSurname());
        existingPatient.setPhone(patientEntity.getPhone());
        patientService.savePatient(existingPatient);

        return "admin_edit";
    }


    @GetMapping("/add")
    public String adminAddPage(Model model, Authentication authentication ) {
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




}
