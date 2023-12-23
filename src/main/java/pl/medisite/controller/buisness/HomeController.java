package pl.medisite.controller.buisness;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@AllArgsConstructor
@Slf4j
public class HomeController {

    @GetMapping()
    public String homePage(HttpSession session, Authentication authentication) {
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
        session.setAttribute("userRole", userRole);
        session.setAttribute("userEmail", userEmail);
        log.info("### UserRole:" + userRole);
        return "home";
    }
}
