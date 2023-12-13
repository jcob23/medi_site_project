package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.buisness.PersonDTO;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.repository.PersonRepository;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PersonRepository personRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity saveUser (UserDTO user, int roleId) {
        UserEntity userEntity = UserEntity.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(roleRepository.getRoleById(roleId))
                .build();

        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity findByEmail(String email){
        return userRepository.findByEmail(email);
    }


    @Transactional
    public List<PersonDTO> getUsersEmails () {
        return personRepository.findPersonsInformation2().stream().filter(user -> user.getRole().equals("USER")).toList();
    }
    @Transactional
    public List<PersonDTO> getDoctorsEmails () {
        return personRepository.findPersonsInformation().stream().filter(user -> user.getRole().equals("DOCTOR")).toList();
    }
    @Transactional
    public List<PersonDTO> getAllEmails () {
        List<PersonDTO> personsInformation = personRepository.findPersonsInformation();
        List<PersonDTO> personsInformation2 = personRepository.findPersonsInformation2();
        personsInformation.addAll(personsInformation2);

        return  personsInformation;
    }

    @Transactional
    public void deleteUser (String email) {
        userRepository.deleteByEmail(email);
    }
}
