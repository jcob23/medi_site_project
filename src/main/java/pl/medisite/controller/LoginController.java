package pl.medisite.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.controller.buisness.PatientDTO;
import pl.medisite.infrastructure.security.ResetPasswordService;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.service.PatientService;
import pl.medisite.service.UserService;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class LoginController {

    private PatientService patientService;
    private UserService userService;
    private ResetPasswordService resetPasswordService;

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

    @GetMapping("/reset")
    public String showResetPasswordPage(){
        return "resetPasswordPage";
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam("email") String mail, Model model) throws MessagingException {
        UserEntity user = userService.findByEmail(mail);
        if(user == null){
            model.addAttribute("notFound",true);
        }else {
            resetPasswordService.sendMail(mail);
            model.addAttribute("reset", true);
        }
        return "resetPasswordPage";
    }

}
