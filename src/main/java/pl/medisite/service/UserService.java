package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.NewUserDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.mapper.DoctorEntityMapper;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private PasswordEncoder passwordEncoder;

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserEntity saveUser(NewUserDTO user, int roleId) {
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

    public Set<PersonDTO> getAllPatients() {
        return patientRepository.getAllPatients().stream()
                .map(PatientEntityMapper::map)
                .collect(Collectors.toSet());
    }

    public Set<PersonDTO.DoctorDTO> getAllDoctors() {
        return doctorRepository.getAllDoctors().stream()
                .map(DoctorEntityMapper::map)
                .collect(Collectors.toSet());
    }

    public Set<PersonDTO> getAllUsersInformation() {
        Set<PersonDTO.DoctorDTO> personInformation = getAllDoctors();
        Set<PersonDTO> personDTO2 = getAllPatients();
        personDTO2.addAll(personInformation);

        return personDTO2;
    }


    public UserEntity getUserFromToken(UUID token) {
        return userRepository.findByToken(token);
    }
}
