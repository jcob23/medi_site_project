package pl.medisite.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.medisite.controller.LoginController;
import pl.medisite.infrastructure.security.UserRepository;
import pl.medisite.service.UserService;

@ExtendWith(MockitoExtension.class)
public class LoginControllerMockitoTest {

    @Mock
    UserRepository userRepository;
    @Mock
    UserService userService;

    @InjectMocks
    private LoginController loginController;

//    @Test
//    void loginPageShowsCorrectly(){
        //when
//        String result = loginController.login();
//        //then
//        Assertions.assertThat(result).isEqualTo("login");
//    }

//    @Test
//    void registerPageShowsCorrectly(){
//        ExtendedModelMap model = new ExtendedModelMap();
//        //when
//        String result = loginController.showRegisterPage( model);
//        //then
//        Assertions.assertThat(result).isEqualTo("register");
//        Assertions.assertThat(model.getAttribute("userDTO")).isEqualTo(new UserDTO());
//    }
//    @Test
//    void registerUserWorksCorrectly () {
//        //given
//        ExtendedModelMap model = new ExtendedModelMap();
//        UserDTO userDTO = UserFixtures.userDTO1();
//        //when
//        String result = loginController.registerUser(userDTO, model);
//        //then
//        Assertions.assertThat(result).isEqualTo("login");
//        Assertions.assertThat(model.getAttribute("registered")).isEqualTo(true);
//    }
}
