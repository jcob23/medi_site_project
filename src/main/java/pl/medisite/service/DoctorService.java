package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.*;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService {

    DoctorRepository doctorRepository;
    UserService userService;
    AppointmentService appointmentService;
    DiseaseRepository diseaseRepository;

    @Transactional
    public void saveDoctor(DoctorDTO doctorDTO) {
        UserEntity userEntity = userService
                .saveUser(UserDTO.builder().email(doctorDTO.getEmail()).password(doctorDTO.getPassword()).build(),3);

        DoctorEntity doctorEntity = DoctorEntity.builder()
                .name(doctorDTO.getName())
                .surname(doctorDTO.getSurname())
                .phone(doctorDTO.getPhone())
                .specialization(doctorDTO.getSpecialization())
                .loginDetails(userEntity)
                .build();
        doctorRepository.save(doctorEntity);
    }

    @Transactional
    public void updateDoctor(DoctorEntity doctorEntity) {
        DoctorEntity existingDoctor = doctorRepository.findByEmail(doctorEntity.getLoginDetails().getEmail());
        existingDoctor.setName(doctorEntity.getName());
        existingDoctor.setSurname(doctorEntity.getSurname());
        existingDoctor.setPhone(doctorEntity.getPhone());
        doctorRepository.save(existingDoctor);
    }

    @Transactional
    public void deleteDoctor(String email) {
        doctorRepository.deleteByMail(email);
        userService.deleteUser(email);
    }

    public DoctorEntity findByEmail(String email) {
        return doctorRepository.findByEmail(email);
    }

    public void addDiseaseToPatientByAppointmentId(Integer appointmentId, DiseaseDTO diseaseDTO) {
        Set<DiseaseEntity> patientDiseases = appointmentService.getPatientDiseases(appointmentId);
        PatientEntity patient = appointmentService.getPatient(appointmentId);
        DiseaseEntity diseaseEntity = DiseaseEntity.mapDTO(diseaseDTO, patient);
        patientDiseases.add(diseaseEntity);
        diseaseRepository.saveAllAndFlush(patientDiseases);
    }

    public Set<PersonDTO> getPatients(String email) {
        return doctorRepository.getPatientsForDoctor(email)
                .stream()
                .map(PatientEntityMapper::map)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getPatientsAppointmentForDoctor(String patientEmail, String doctorEmail) {
        return appointmentService.getPatientFutureAppointmentsForDoctor(patientEmail,doctorEmail);
    }
}
