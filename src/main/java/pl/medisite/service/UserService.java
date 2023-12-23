package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.entity.PersonInformation;
import pl.medisite.infrastructure.database.repository.PersonInformationRepository;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PersonInformationRepository personInformationRepository;
    PasswordEncoder passwordEncoder;

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserEntity saveUser(UserDTO user, int roleId) {
        UserEntity userEntity = UserEntity.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(roleRepository.getRoleById(roleId))
                .build();

        return userRepository.saveAndFlush(userEntity);
    }

    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void changeUserPassword(UserEntity user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public List<PersonInformation> getPatientsInformation() {
        return personInformationRepository.findPatientInformation();
    }

    public List<PersonInformation.DoctorInformation> getDoctorsInformation() {
        return personInformationRepository.findDoctorInformation();
    }

    @Transactional
    public List<PersonInformation> getAllUsersInformation() {
        List<PersonInformation.DoctorInformation> personInformation = personInformationRepository.findDoctorInformation();
        List<PersonInformation> personInformation2 = personInformationRepository.findPatientInformation();
        personInformation2.addAll(personInformation);

        return personInformation2;
    }


    public UserEntity getUserFromToken(UUID token) {
        return userRepository.findByToken(token);
    }
}
