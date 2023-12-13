package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.buisness.PatientDTO;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.UserEntity;

@Service
@AllArgsConstructor
public class PatientService {

    private UserService userService;
    private PatientRepository patientRepository;

    @Transactional
    public void savePatient(PatientDTO patientDTO) {
        UserEntity userEntity = userService
                .saveUser(UserDTO.builder().email(patientDTO.getEmail()).password(patientDTO.getPassword()).build(), 2);

        PatientEntity patientEntity = PatientEntity.builder()
                .name(patientDTO.getName())
                .surname(patientDTO.getSurname())
                .phone(patientDTO.getPhone())
                .loginDetails(userEntity)
                .build();
        patientRepository.save(patientEntity);
    }

    public PatientEntity findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
}
