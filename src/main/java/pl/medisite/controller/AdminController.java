package pl.medisite.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.medisite.controller.DTO.NewPatientDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.service.DoctorService;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;
import pl.medisite.util.Constants;

import java.util.AbstractMap;
import java.util.List;

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
        model.addAttribute("newDoctorDTO", new NewPatientDTO.NewDoctorDTO());
        return "admin_add";
    }

    @PostMapping("/add")
    public String adminAddDoctor(
            @Valid @ModelAttribute("doctorDTO") NewPatientDTO.NewDoctorDTO newDoctorDTO,
            RedirectAttributes redirectAttributes) {
        doctorService.saveDoctor(newDoctorDTO);
        redirectAttributes.addFlashAttribute("added", true);
        return "redirect:/admin/add";
    }

    @GetMapping("/patients")
    public String adminPatientsPage(Model model, @RequestParam(defaultValue = "1") Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC, "surname"));
        AbstractMap.SimpleEntry<Integer, List<PersonDTO>> users = userService.getAllPatients(pageable);
        model.addAttribute("patientView", true);
        model.addAttribute("personsData", users.getValue());
        model.addAttribute("numberOfPages", users.getKey());
        model.addAttribute("url", "admin/patients");
        return "admin_list";
    }

    @GetMapping("/doctors")
    public String adminDoctorsPage(Model model, @RequestParam(defaultValue = "1") Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC, "surname"));
        AbstractMap.SimpleEntry<Integer, List<PersonDTO.DoctorDTO>> users = userService.getAllDoctors(pageable);
        model.addAttribute("doctorView", true);
        model.addAttribute("personsData", users.getValue());
        model.addAttribute("numberOfPages", users.getKey());
        model.addAttribute("url", "admin/doctors");
        return "admin_list";
    }


    @GetMapping("/users")
    public String adminUsersPage(Model model, @RequestParam(defaultValue = "1") Integer page) {
        PageRequest pageable = PageRequest.of(page - 1, Constants.ELEMENTS_ON_PAGE, Sort.by(Sort.Direction.ASC, "surname"));
        AbstractMap.SimpleEntry<Integer, List<PersonDTO>> users = userService.getAllUsersInformation(pageable);
        model.addAttribute("personsData", users.getValue());
        model.addAttribute("numberOfPages", users.getKey());
        model.addAttribute("url", "admin/users");
        return "admin_list";
    }
}
