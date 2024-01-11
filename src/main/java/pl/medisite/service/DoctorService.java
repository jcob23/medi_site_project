package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.NewPatientDTO;
import pl.medisite.controller.DTO.NewUserDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.mapper.DoctorEntityMapper;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService {

    DoctorRepository doctorRepository;
    AppointmentRepository appointmentRepository;
    UserService userService;

    public DoctorEntity checkIfDoctorExist(String email) {
        DoctorEntity doctor = doctorRepository.findByEmail(email);
        if( Objects.isNull(doctor) ) {
            throw new EntityNotFoundException("Nie ma doktora o takim adresie email");
        }
        return doctor;
    }

    public PersonDTO.DoctorDTO getDoctor(String email) {
        return DoctorEntityMapper.map(checkIfDoctorExist(email));
    }

    @Transactional
    public void saveDoctor(NewPatientDTO.NewDoctorDTO newDoctorDTO) {
        UserEntity userEntity = userService
                .saveUser(NewUserDTO.builder().email(newDoctorDTO.getEmail()).password(newDoctorDTO.getPassword()).build(), 3);

        DoctorEntity doctorEntity = DoctorEntity.builder()
                .name(newDoctorDTO.getName())
                .surname(newDoctorDTO.getSurname())
                .phone(newDoctorDTO.getPhone())
                .specialization(newDoctorDTO.getSpecialization())
                .description("")
                .loginDetails(userEntity)
                .build();
        doctorRepository.save(doctorEntity);
    }

    @Transactional
    public void updateDoctor(PersonDTO.DoctorDTO doctorEntity) {
        DoctorEntity existingDoctor = doctorRepository.findByEmail(doctorEntity.getEmail());
        existingDoctor.setName(doctorEntity.getName());
        existingDoctor.setSurname(doctorEntity.getSurname());
        existingDoctor.setPhone(doctorEntity.getPhone());
        existingDoctor.setSpecialization(doctorEntity.getSpecialization());
        existingDoctor.setDescription(doctorEntity.getDescription());
        doctorRepository.save(existingDoctor);
    }

    @Transactional
    public void deleteDoctor(String email) {
        appointmentRepository.deleteAllByDoctorEmail(email);
        doctorRepository.deleteByMail(email);
        userService.deleteUser(email);
    }


    public Set<PersonDTO> getPatients(String email) {
        return doctorRepository.getPatientsForDoctor(email)
                .stream()
                .map(PatientEntityMapper::map)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


}
