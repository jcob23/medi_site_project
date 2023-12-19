package pl.medisite.controller.buisness;

import jakarta.servlet.http.HttpSession;
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
    public String homePage (HttpSession session, Authentication authentication) {
        String userRole = authentication.getAuthorities().iterator().next().getAuthority();
        log.error("### " + userRole);
        session.setAttribute("userRole", userRole);
        return "home";
    }
}
