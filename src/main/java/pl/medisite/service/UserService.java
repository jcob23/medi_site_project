package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.UserDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.mapper.DoctorEntityMapper;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PatientRepository patientRepository;
    DoctorRepository doctorRepository;
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

    public Set<PersonDTO> getPatientsInformation() {
        return patientRepository.getAllPatients().stream()
                .map(PatientEntityMapper::map)
                .collect(Collectors.toSet());
    }

    public Set<PersonDTO.DoctorDTO> getDoctorsInformation() {
        return doctorRepository.getAllDoctors().stream()
                .map(DoctorEntityMapper::map)
                .collect(Collectors.toSet());
    }

    public Set<PersonDTO> getAllUsersInformation() {
        Set<PersonDTO.DoctorDTO> personInformation = getDoctorsInformation();
        Set<PersonDTO> personDTO2 = getPatientsInformation();
        personDTO2.addAll(personInformation);

        return personDTO2;
    }


    public UserEntity getUserFromToken(UUID token) {
        return userRepository.findByToken(token);
    }
}
