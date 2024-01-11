package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.NewPatientDTO;
import pl.medisite.controller.DTO.NewUserDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class PatientService {

    private UserService userService;
    private PatientRepository patientRepository;
    private DiseaseRepository diseaseRepository;
    private AppointmentRepository appointmentRepository;

    public PatientEntity checkIfPatientExist(String email) {
        PatientEntity patient = patientRepository.findByEmail(email);
        if( Objects.isNull(patient) ) {
            throw new EntityNotFoundException("Nie ma pacjenta o takim adresie email: " + email);
        }
        return patient;
    }

    public PersonDTO getPatient(String email) {
        return PatientEntityMapper.map(checkIfPatientExist(email));
    }

    @Transactional
    public void savePatient(NewPatientDTO newPatientDTO) {
        UserEntity userEntity = userService
                .saveUser(NewUserDTO.builder().email(newPatientDTO.getEmail().stripTrailing()).password(newPatientDTO.getPassword()).build(), 2);

        PatientEntity patientEntity = PatientEntity.builder()
                .name(newPatientDTO.getName())
                .surname(newPatientDTO.getSurname())
                .phone(newPatientDTO.getPhone().stripTrailing())
                .loginDetails(userEntity)
                .build();
        patientRepository.save(patientEntity);
    }

    @Transactional
    public void updatePatient(PersonDTO patient) {
        PatientEntity existingPatient = checkIfPatientExist(patient.getEmail());
        existingPatient.setName(patient.getName());
        existingPatient.setSurname(patient.getSurname());
        existingPatient.setPhone(patient.getPhone());
        patientRepository.save(existingPatient);
    }

    @Transactional
    public void deletePatient(String email) {
        List<AppointmentEntity> patientAppointments = appointmentRepository.getPatientAppointments(email);
        patientAppointments.forEach(appointment -> appointment.setPatient(null));
        appointmentRepository.saveAllAndFlush(patientAppointments);
        diseaseRepository.deleteAllByEmail(email);
        patientRepository.deleteByMail(email);
        userService.deleteUser(email);
    }
}
