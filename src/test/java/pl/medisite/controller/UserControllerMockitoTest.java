package pl.medisite.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.medisite.infrastructure.security.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserControllerMockitoTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
   void thatShowingRegisterPageWorksCorrectly(){
        //given
        //when
        //then
    }
}
