package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.NewUserDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.DoctorEntityMapper;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.AbstractMap;
import java.util.List;
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
                .role(roleRepository.findById(roleId).orElseThrow(()-> new EntityNotFoundException("Role Not Found")))
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

    public List<PersonDTO> getAllPatients() {
        return patientRepository.findAll().stream()
                .map(PatientEntityMapper::map)
                .collect(Collectors.toList());
    }
    public AbstractMap.SimpleEntry<Integer,List<PersonDTO>> getAllPatients(Pageable pageable) {
        Page<PatientEntity> allData = patientRepository.findAll(pageable);
        Page<PersonDTO> doctors = allData.map(PatientEntityMapper::map);
        return new AbstractMap.SimpleEntry<>(doctors.getTotalPages(),doctors.getContent());
    }

    public List<PersonDTO.DoctorDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(DoctorEntityMapper::map)
                .collect(Collectors.toList());
    }
    public AbstractMap.SimpleEntry<Integer,List<PersonDTO.DoctorDTO>> getAllDoctors(Pageable pageable) {
        Page<DoctorEntity> allData = doctorRepository.findAll(pageable);
        Page<PersonDTO.DoctorDTO> doctors = allData.map(DoctorEntityMapper::map);
        return new AbstractMap.SimpleEntry<>(doctors.getTotalPages(),doctors.getContent());
    }

    public AbstractMap.SimpleEntry<Integer,List<PersonDTO>> getAllUsersInformation(Pageable pageable) {
        List<PersonDTO.DoctorDTO> personInformation = getAllDoctors();
        List<PersonDTO> personDTO2 = getAllPatients();
        personDTO2.addAll(personInformation);

        Page<PersonDTO> page = new PageImpl<>(personDTO2,pageable,personDTO2.size());
        return new AbstractMap.SimpleEntry<>(page.getTotalPages(),page.getContent());
    }
    public UserEntity getUserFromToken(UUID token) {
        return userRepository.findByToken(token);
    }
}
