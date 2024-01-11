package pl.medisite.controller.WebMVC;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.medisite.controller.ResetPasswordController;
import pl.medisite.infrastructure.security.ForgotPassword.ResetPasswordService;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.service.UserService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ResetPasswordController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ResetPasswordControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ResetPasswordService resetPasswordService;


    @Test
    void forgetPasswordPageWorksCorrectly() throws Exception {
        mockMvc.perform(get("/forget"))
                .andExpect(status().isOk())
                .andExpect(view().name("forget_password"));
    }

    @Test
    void sendForgetPasswordMail_UserNotFound() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(null);

        mockMvc.perform(post("/forget").param("email", "nonexistent@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("forget_password"))
                .andExpect(model().attribute("notFound", true));

        verify(resetPasswordService, never()).sendMail(anyString());
    }
    @Test
    void sendForgetPasswordMail_UserFound() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(new UserEntity());

        mockMvc.perform(post("/forget").param("email", "existing@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("forget_password"))
                .andExpect(model().attribute("mail", true));

        verify(resetPasswordService, times(1)).sendMail("existing@example.com");
    }

    @Test
    void showResetPasswordPage() throws Exception {
        String token = "someToken";

        mockMvc.perform(get("/forget/reset").param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPassword"))
                .andExpect(request().sessionAttribute("token", token));
    }
    @Test
    void resetUserPassword() throws Exception {
        // Ustaw dane sesji
        String newPassword = "newPassword";
        UUID token = UUID.randomUUID();

        UserEntity mockUser = new UserEntity();
        when(userService.getUserFromToken(token)).thenReturn(mockUser);

        mockMvc.perform(post("/forget/reset")
                        .param("password", newPassword)
                        .sessionAttr("token", token.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("reset", true));

        verify(userService, times(1)).changeUserPassword(eq(mockUser), eq(newPassword));
    }




}
