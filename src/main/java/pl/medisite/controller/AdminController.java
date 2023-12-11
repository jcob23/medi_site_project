package pl.medisite.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.medisite.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private UserService userService;
    @GetMapping()
    public String adminPage(Model model){
        List<String> users = userService.getUsersEmails();
        model.addAttribute("usersEmails",users );
        return "admin";
    }
    @DeleteMapping("/delete/{email}")
    public String deleteUser(@PathVariable String email){
        userService.deleteUser(email);
        return "redirect:/admin";
    }


}
