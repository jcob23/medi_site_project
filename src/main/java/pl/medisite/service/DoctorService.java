package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.DiseaseDTO;
import pl.medisite.controller.DTO.DoctorDTO;
import pl.medisite.controller.DTO.UserDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
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
                .saveUser(UserDTO.builder().email(doctorDTO.getEmail()).password(doctorDTO.getPassword()).build(), 3);

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

    public Set<AppointmentEntity> getAppointments(String email) {
       return appointmentService.getDoctorAppointments(email);
    }

    public Set<AppointmentDTO> getAppointmentsDTO(String email) {
        return appointmentService.getDoctorAppointments(email).stream().map(AppointmentDTO::mapAppointment).
                collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void addDiseaseToPatientByAppointmentId(Integer appointmentId, DiseaseDTO diseaseDTO) {
        Set<DiseaseEntity> patientDiseases = appointmentService.getPatientDiseases(appointmentId);
        PatientEntity patient = appointmentService.getPatient(appointmentId);
        DiseaseEntity diseaseEntity = DiseaseEntity.mapDTO(diseaseDTO, patient);
        patientDiseases.add(diseaseEntity);
        diseaseRepository.saveAllAndFlush(patientDiseases);
    }
}
