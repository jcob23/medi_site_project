package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;
import pl.medisite.util.UserFixtures;

import java.util.List;

import static org.junit.Assert.assertThrows;


@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryJpaTest extends AbstractJpaIT {

    private UserRepository userRepository;

    @Test
    void userCanBeSavedCorrectly() {
        //given
        List<UserEntity> users = List.of(UserFixtures.userEntity1(), UserFixtures.userEntity2(), UserFixtures.userEntity3());
        userRepository.saveAllAndFlush(users);
        //when
        List<UserEntity> foundUsers = userRepository.findAll();
        //then
        Assertions.assertThat(foundUsers.size()).isEqualTo(8); // 8 becosue 5 useres added in migrations

    }

    @Test
    void userCanBeFoundByEmail() {
        //given
        UserEntity user = UserFixtures.userEntity1();
        userRepository.saveAndFlush(user);

        //when
        UserEntity foundUser = userRepository.findByEmail(user.getEmail());

        //then
        Assertions.assertThat(foundUser).isEqualTo(user);
    }

    @Test
    void userRoleRetrievedCorrectly() {
        //given
        UserEntity user = UserFixtures.userEntity1();
        userRepository.saveAndFlush(user);

        //when
        UserEntity foundUser = userRepository.findByEmail(user.getEmail());

        //then
        Assertions.assertThat(foundUser.getRole()).isNotNull();
    }

    @Test
    void userCanBeDeleted() {
        //given
        UserEntity user = UserFixtures.userEntity1();
        userRepository.saveAndFlush(user);

        //when
        userRepository.deleteByEmail(user.getEmail());
        List<UserEntity> result = userRepository.findAll();

        //then
        Assertions.assertThat(result).doesNotContain(user);
    }

//    @Test
//    void userUniqueKeyWorkingCorrectly() {
//        //given
//        UserEntity user = UserFixtures.userEntity1();;
//        userRepository.save(user);
//        UserEntity user2 = UserFixtures.userEntity1();
//
//
//        assertThrows(DataIntegrityViolationException.class, () ->  userRepository.save(user2););
//
//         //then
//        List<UserEntity> all = userRepository.findAll();
//        Assertions.assertThat(all).hasSize(6);
//    }


}
