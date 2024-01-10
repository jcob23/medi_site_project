package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.DTO.NewPatientDTO;
import pl.medisite.service.PatientService;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class LoginController {

    private PatientService patientService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error,
                            HttpSession session,
                            Model model) {
        if( error != null ) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("newPatientDTO", new NewPatientDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("patientDTO") NewPatientDTO newPatientDTO,
            Model model
    ) {
        patientService.savePatient(newPatientDTO);
        model.addAttribute("registered", true);
        return "login";
    }


}
