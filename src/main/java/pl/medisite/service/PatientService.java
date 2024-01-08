package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.PatientDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.controller.DTO.UserDTO;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.Objects;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {

    private UserService userService;
    private PatientRepository patientRepository;

    @Transactional
    public void savePatient(PatientDTO patientDTO) {
        UserEntity userEntity = userService
                .saveUser(UserDTO.builder().email(patientDTO.getEmail().stripTrailing()).password(patientDTO.getPassword()).build(), 2);

        PatientEntity patientEntity = PatientEntity.builder()
                .name(patientDTO.getName())
                .surname(patientDTO.getSurname())
                .phone(patientDTO.getPhone().stripTrailing())
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

    public PatientEntity checkIfPatientExist(String email) {
        PatientEntity patient = patientRepository.findByEmail(email);
        if( Objects.isNull(patient) ) {
            throw new EntityNotFoundException("Nie ma pacjenta o takim adresie email");
        }
        return patient;
    }

    public PatientEntity getPatient(Integer appointmentId) {
        return patientRepository.getPatientByAppointmentId(appointmentId);
    }



    public PersonDTO getPatient(String email){
        return PatientEntityMapper.map(patientRepository.findByEmail(email));
    }

    public PatientEntity getPatientByAppointmentId(Integer appointmentId) {
        return patientRepository.getPatientByAppointmentId(appointmentId);
    }
}
