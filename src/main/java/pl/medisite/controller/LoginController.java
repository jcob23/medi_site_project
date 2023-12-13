package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.buisness.PatientDTO;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class LoginController {

    private PatientService patientService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String loginPage(@RequestParam(name = "error", required = false) String error,
                            HttpSession session,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage"); // Usuwamy atrybut z sesji po użyciu
        }
        return "login";
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model){
        model.addAttribute("patientDTO",new PatientDTO());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("patientDTO") PatientDTO patientDTO,
            Model model
    ){
        patientService.savePatient(patientDTO);
        model.addAttribute("registered", true);
        return "login";
    }

}
