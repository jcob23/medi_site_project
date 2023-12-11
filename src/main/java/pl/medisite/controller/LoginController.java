package pl.medisite.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.service.UserService;

@Controller
@RequestMapping("")
@AllArgsConstructor
public class LoginController {

    private UserService userService;
    @GetMapping("/login")
    public String login(){
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


}
