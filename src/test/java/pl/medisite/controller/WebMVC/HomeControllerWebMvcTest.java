package pl.medisite.controller.WebMVC;

import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.medisite.controller.HomeController;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class HomeControllerWebMvcTest {


    private WebApplicationContext context;

    private MockMvc webMvc;

    @Before
    public void setup() {
        webMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(roles = "PATIENT", username = "testPatient@medisite.pl")
    @Test
    public void testHomePageWithPatient() throws Exception {

        webMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(request().sessionAttribute("userRole", "ROLE_PATIENT"))
                .andExpect(request().sessionAttribute("userEmail", "testPatient@medisite.pl"));
    }


}
