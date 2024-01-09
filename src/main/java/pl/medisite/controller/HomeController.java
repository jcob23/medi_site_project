package pl.medisite.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
@AllArgsConstructor
@Slf4j
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
