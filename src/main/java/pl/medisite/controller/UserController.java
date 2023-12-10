package pl.medisite.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.service.UserService;

import java.util.List;

@Controller
@RequestMapping("")
@AllArgsConstructor
@Slf4j
public class UserController {

    private UserService userService;
    @GetMapping("/medi_login")
    public String login(){
        return "medi_login";
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
        return "medi_login";
    }
    @GetMapping("/admin")
    public String adminPage(Model model){
        List<String> users = userService.getUsersEmails();
        model.addAttribute("usersEmails",users );
        return "admin";
    }
    @DeleteMapping("/admin/delete/{email}")
    public String deleteUser(@PathVariable String email){
        userService.deleteUser(email);
        return "redirect:/admin";
    }
    @GetMapping("/user")
    public String userPage(){
        return "user";
    }
}
