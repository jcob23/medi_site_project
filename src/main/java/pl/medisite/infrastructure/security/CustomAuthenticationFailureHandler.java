package pl.medisite.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import pl.medisite.service.UserService;


import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String targetUrl = "/login?error=true";
        setFailureMessage(request, determineFailureMessage(request, exception));
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void setFailureMessage(HttpServletRequest request, String errorMessage) {
        request.getSession().setAttribute("errorMessage", errorMessage);
    }

    private String determineFailureMessage(HttpServletRequest request, AuthenticationException exception) {
        String mail = request.getParameter("username");
        UserEntity byEmail = userService.findByEmail(mail);
        return (byEmail == null) ?
                String.format("Nie znaleziono konta o takim mailu: %s.", mail) :
                "Błędne hasło.";
    }
}