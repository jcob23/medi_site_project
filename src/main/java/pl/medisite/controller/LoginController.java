package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.service.UserService;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class LoginController {

    private UserService userService;

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
        model.addAttribute("userDTO",new UserDTO());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userDTO") UserDTO userDTO,
            Model model
    ){
        userService.saveUser(userDTO);
        model.addAttribute("registered", true);
        return "login";
    }

    public record LoginRequest(String username, String password) {
    }


}
