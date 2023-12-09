package pl.medisite.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.service.LoginService;

import java.sql.SQLOutput;

@Controller
@RequestMapping("")
@AllArgsConstructor
@Slf4j
public class LoginController {

    private LoginService loginService;

    @GetMapping("/register")
    public String showRegisterPage(Model model){
        return "register";
    }

    @PostMapping("/register/save")
    public String registerUser(
            @RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password
            ){
        UserDTO user = new UserDTO(password,email);
        loginService.saveUser(user);
        return "login";
    }
    @GetMapping("/login")
    public String login(Model model){
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage(Model model){
            return "admin";
    }
    @GetMapping("/user")
    public String userPage(Model model){
        return "user";
    }
}
