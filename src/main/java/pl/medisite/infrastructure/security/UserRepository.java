package pl.medisite.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.medisite.controller.buisness.PersonDTO;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByEmail(String email);

    void deleteByEmail (String email);


}
