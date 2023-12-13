package pl.medisite.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
@Slf4j
public class HomeController {

    @GetMapping()
    public String homePage (Model model, Authentication authentication) {
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        log.error("### " + userRole);
        model.addAttribute("userRole", userRole);
        return "home";
    }
}
