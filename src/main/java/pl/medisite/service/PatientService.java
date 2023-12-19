package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.buisness.PatientDTO;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {

    private UserService userService;
    private PatientRepository patientRepository;
    private DiseaseRepository diseaseRepository;

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

    @Transactional
    public void updatePatient(PatientEntity patientEntity) {
        PatientEntity existingPatient = patientRepository.findByEmail(patientEntity.getLoginDetails().getEmail());
        existingPatient.setName(patientEntity.getName());
        existingPatient.setSurname(patientEntity.getSurname());
        existingPatient.setPhone(patientEntity.getPhone());
        patientRepository.save(existingPatient);
    }


    @Transactional
    public void deletePatient(String email) {
        patientRepository.deleteByMail(email);
        userService.deleteUser(email);
    }

    public Set<DiseaseEntity> getDiseases(String email){
        return diseaseRepository.getDiseases(email);
    }
    public PatientEntity findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }
}
