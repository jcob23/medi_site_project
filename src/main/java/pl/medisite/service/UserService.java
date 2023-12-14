package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.PersonInformation;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.repository.PersonInformationRepository;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PersonInformationRepository personInformationRepository;
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
    @Transactional
    public List<PersonInformation> getUsersEmails () {
        return personInformationRepository.findPatientInformation().stream().filter(user -> user.getRole().equals("USER")).toList();
    }
    @Transactional
    public List<PersonInformation> getDoctorsEmails () {
        return personInformationRepository.findDoctorInformation().stream().filter(user -> user.getRole().equals("DOCTOR")).toList();
    }
    @Transactional
    public List<PersonInformation> getAllEmails () {
        List<PersonInformation> personInformation = personInformationRepository.findDoctorInformation();
        List<PersonInformation> personInformation2 = personInformationRepository.findPatientInformation();
        personInformation.addAll(personInformation2);

        return personInformation;
    }

    @Transactional
    public void deleteUser (String email) {
        userRepository.deleteByEmail(email);
    }
}
