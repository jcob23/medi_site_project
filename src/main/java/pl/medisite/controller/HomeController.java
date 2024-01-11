package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String homePage(HttpSession session, Authentication authentication) {
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        session.setAttribute("userRole", userRole);
        session.setAttribute("userEmail", userEmail);
        return "home";
    }
}
