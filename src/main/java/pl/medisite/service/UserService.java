package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.UserDTO;
import pl.medisite.controller.DTO.PersonDTO;
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

    public List<PersonDTO> getPatientsInformation() {
        return personInformationRepository.findPatientInformation();
    }

    public List<PersonDTO.DoctorDTO> getDoctorsInformation() {
        return personInformationRepository.findDoctorInformation();
    }

    @Transactional
    public List<PersonDTO> getAllUsersInformation() {
        List<PersonDTO.DoctorDTO> personInformation = personInformationRepository.findDoctorInformation();
        List<PersonDTO> personDTO2 = personInformationRepository.findPatientInformation();
        personDTO2.addAll(personInformation);

        return personDTO2;
    }


    public UserEntity getUserFromToken(UUID token) {
        return userRepository.findByToken(token);
    }
}
