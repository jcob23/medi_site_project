package pl.medisite.controller.system;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.medisite.infrastructure.security.ForgotPassword.ResetPasswordService;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.service.UserService;

import java.util.UUID;

@Controller
@RequestMapping("/forget")
@AllArgsConstructor
@Slf4j
public class ResetPasswordController {

    private UserService userService;
    private ResetPasswordService resetPasswordService;


    @GetMapping()
    public String showForgetPasswordPage() {
        return "forgetPassword";
    }

    @PostMapping()
    public String sendForgetPasswordMail(@RequestParam("email") @Email String mail, Model model) throws MessagingException {
        UserEntity user = userService.findByEmail(mail);
        if( user == null ) {
            model.addAttribute("notFound", true);
        } else {
            resetPasswordService.sendMail(mail);
            model.addAttribute("mail", true);
        }
        return "forgetPassword";
    }

    @GetMapping("/reset")
    public String showResetPasswordPage(
            @RequestParam(name = "token") String token,
            HttpSession session

    ) {
        session.setAttribute("token", token);
        return "resetPassword";
    }

    @PostMapping("/reset")
    public String resetUserPassword(
            @RequestParam(name = "password") String newPassword,
            HttpSession session,
            Model model
    ) {
        model.addAttribute("reset", true);
        UUID token = UUID.fromString((String) session.getAttribute("token"));
        session.removeAttribute("token");
        UserEntity user = userService.getUserFromToken(token);
        userService.changeUserPassword(user,newPassword);
        return "login";
    }
}
