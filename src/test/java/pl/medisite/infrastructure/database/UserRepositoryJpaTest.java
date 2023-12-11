package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;
import pl.medisite.util.UserFixtures;

import java.util.List;


@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryJpaTest extends AbstractJpaIT {

    private UserRepository userRepository;
    @Test
    void userCanBeSavedCorrectly () {
        //given
        List<UserEntity> users = List.of(UserFixtures.userEntity1(), UserFixtures.userEntity2(), UserFixtures.userEntity3());
        userRepository.saveAllAndFlush(users);
        //when
        List<UserEntity> foundUsers = userRepository.findAll();
        //then
        Assertions.assertThat(foundUsers.size()).isEqualTo(4); // 4 ponieważ admin dodany w migracjach

    }
    @Test
    void userCanBeFoundByEmail(){
        //given
        UserEntity user = UserFixtures.userEntity1();
        userRepository.saveAndFlush(user);

        //when
        UserEntity foundUser = userRepository.findByEmail(user.getEmail());

        //then
        Assertions.assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void userCanBeDeleted(){
        //given
        UserEntity user = UserFixtures.userEntity1();
        userRepository.saveAndFlush(user);

        //when
        userRepository.deleteByEmail(user.getEmail());
        List<UserEntity> result = userRepository.findAll();

        //then
        Assertions.assertThat(result).doesNotContain(user);
    }
}
