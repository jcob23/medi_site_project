package pl.medisite.controller.WebMVC;


import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import pl.medisite.controller.DTO.NewPatientDTO;
import pl.medisite.controller.LoginController;
import pl.medisite.service.PatientService;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// WebMvcTest-test integracyjny
// Poprawne mapowanie url
// Deserializacja parametrów wejściowych (z requestu Http na metody)
// Serializacja parametrów wyjściowych (z klasy na wyjściowego html)
// Walidacja danych wejściowych
// Odpowiednie zmapowanie wyjątków
@WebMvcTest(controllers = LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LoginControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;


    @Test
    void loginPageWorksCorrectly() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void registerPageWorksCorrectly() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("newPatientDTO"))
                .andExpect(view().name("register"));
    }


    @ParameterizedTest
    @MethodSource("phoneValidationWorksCorrectly")
    void phoneValidationWorksCorrectly(Boolean correctPhone, String phone) throws Exception {

        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = NewPatientDTO.builder().phone(phone).build().asMap();
        parametersMap.forEach(parameters::add);

        if( correctPhone ) {
            mockMvc.perform(MockMvcRequestBuilders.post("/register").params(parameters))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("registered"))
                    .andExpect(view().name("login"));

        } else {
            mockMvc.perform(MockMvcRequestBuilders.post("/register").params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeDoesNotExist("registered"))
                    .andExpect(view().name("error"));
        }
    }

    @ParameterizedTest
    @MethodSource("emailValidationWorksCorrectly")
    void emailValidationWorksCorrectly(Boolean correctEmail, String email) throws Exception {
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = NewPatientDTO.builder().email(email).build().asMap();
        parametersMap.forEach(parameters::add);

        if( correctEmail ) {
            mockMvc.perform(MockMvcRequestBuilders.post("/register").params(parameters))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("registered"))
                    .andExpect(view().name("login"));

        } else {
            mockMvc.perform(MockMvcRequestBuilders.post("/register").params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeDoesNotExist("registered"))
                    .andExpect(model().attribute("errorMessage", Matchers.containsString(email)))
                    .andExpect(view().name("error"));
        }
    }

    @ParameterizedTest
    @MethodSource("passwordValidationWorksCorrectly")
    void passwordValidationWorksCorrectly(Boolean correctPassword, String password) throws Exception {
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = NewPatientDTO.builder().password(password).build().asMap();
        parametersMap.forEach(parameters::add);

        if( correctPassword ) {
            mockMvc.perform(MockMvcRequestBuilders.post("/register").params(parameters))
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("registered"))
                    .andExpect(view().name("login"));

        } else {
            mockMvc.perform(MockMvcRequestBuilders.post("/register").params(parameters))
                    .andExpect(status().isBadRequest())
                    .andExpect(model().attributeDoesNotExist("registered"))
                    .andExpect(model().attribute("errorMessage", "Za krótkie hasło!"))
                    .andExpect(view().name("error"));
        }
    }

    public static Stream<Arguments> emailValidationWorksCorrectly() {
        return Stream.of(
                /*1*/ Arguments.of(true, "example@email.com"),
                /*2*/ Arguments.of(true, "example.first.middle.lastname@email.com"),
                /*3*/ Arguments.of(true, "example@subdomain.email.com"),
                /*8*/ Arguments.of(true, "0987654321@example.com"),
                /*9*/ Arguments.of(true, "example@email-one.com"),
                /*10*/ Arguments.of(true, "_______@email.com"),
                /*11*/ Arguments.of(true, "example@email.name"),
                /*12*/ Arguments.of(true, "example@email.museum"),
                /*13*/ Arguments.of(true, "example@email.co.jp"),
                /*14*/ Arguments.of(true, "example@email.web"),
                /*15*/ Arguments.of(false, "example+firstname+lastname@email.com"),
                /*16*/ Arguments.of(false, "example@234.234.234.234"),
                /*17*/ Arguments.of(false, "example@[234.234.234.234]"),
                /*18*/ Arguments.of(false, "“example”@email.com"),
                /*19*/ Arguments.of(false, "plaintextaddress"),
                /*20*/ Arguments.of(false, "@#@@##@%^%#$@#$@#.com"),
                /*21*/ Arguments.of(false, "@email.com"),
                /*22*/ Arguments.of(false, "John Doe <example@email.com>"),
                /*23*/ Arguments.of(false, "example.email.com"),
                /*24*/ Arguments.of(false, "example@example@email.com"),
                /*25*/ Arguments.of(false, ".example@email.com"),
                /*26*/ Arguments.of(false, "example.@email.com"),
                /*27*/ Arguments.of(false, "example…example@email.com"),
                /*28*/ Arguments.of(false, "おえあいう@example.com"),
                /*29*/ Arguments.of(false, "example@email.com (John Doe)"),
                /*30*/ Arguments.of(false, "example@email"),
                /*31*/ Arguments.of(false, "example@-email.com"),
                /*32*/ Arguments.of(false, "example@111.222.333.44444"),
                /*33*/ Arguments.of(false, "example@email…com"),
                /*34*/ Arguments.of(false, "CAT…123@email.com"),
                /*35*/ Arguments.of(false, "”(),:;<>[\\]@email.com"),
                /*36*/ Arguments.of(false, "obviously”not”correct@email.com"),
                /*37*/ Arguments.of(false, "example\\ is”especially”not\\allowed@email.com"),
                /*38*/ Arguments.of(false, "")
        );

    }


    public static Stream<Arguments> passwordValidationWorksCorrectly() {
        return Stream.of(
                Arguments.of(false, "123"),
                Arguments.of(false, ""),
                Arguments.of(true, "1234")
        );
    }

    public static Stream<Arguments> phoneValidationWorksCorrectly() {
        return Stream.of(
                Arguments.of(false, "111"),
                Arguments.of(false, ""),
                Arguments.of(false, "sfdg"),
                Arguments.of(false, "111222333"),
                Arguments.of(false, "48111222333"),
                Arguments.of(false, "+48111222333"),
                Arguments.of(false, "+48 111 222 3331"),
                Arguments.of(false, "111-222-333"),
                Arguments.of(false, "+48 111-222-333"),
                Arguments.of(true, "+48 111 222 333")
        );
    }

}
