package pl.medisite.controller.WebMVC;

import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.medisite.controller.HomeController;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.service.DiseaseService;
import pl.medisite.util.SecurityHelper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DiseaseControllerWebMvcTest {

//    private WebApplicationContext context;
//
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
//
//    @MockBean
//    private DiseaseService diseaseService;
//
//    @MockBean
//    private SecurityHelper securityHelper;
//
//    @Test
//    @WithMockUser(username = "patient@example.com")
//    public void testShowPatientDiseases() throws Exception {
//        // Przygotowanie danych testowych
//        String email = "patient@example.com";
//        Set<DiseaseEntity> diseases = new HashSet<>();
//        diseases.add(new DiseaseEntity(1,"testName","testDesc",false, LocalDate.of(2024,1,1),new PatientEntity()));
//
//        // Symulacja działania serwisu
//        when(diseaseService.getDiseases(email)).thenReturn(diseases);
//
//        // Symulacja sprawdzenia dostępu przez securityHelper
//        doNothing().when(securityHelper).checkUserAccessToPatientInformation(eq(email), any(User.class));
//
//        // Wykonaj żądanie HTTP GET na endpoint /patient/diseases/{email}
//        mockMvc.perform(MockMvcRequestBuilders.get("/patient/diseases/{email}", email))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("patient_diseases"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("patientDiseases"));
//
//        // Sprawdź, czy serwis został wywołany raz z poprawnymi argumentami
//        verify(diseaseService, times(1)).getDiseases(email);
//        // Sprawdź, czy securityHelper został wywołany raz z poprawnymi argumentami
//        verify(securityHelper, times(1)).checkUserAccessToPatientInformation(eq(email), any(User.class));
//    }
}
