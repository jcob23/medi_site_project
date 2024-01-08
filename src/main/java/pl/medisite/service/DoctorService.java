package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.*;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.mapper.DoctorEntityMapper;
import pl.medisite.infrastructure.database.mapper.PatientEntityMapper;
import pl.medisite.infrastructure.database.repository.DiseaseRepository;
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
    UserService userService;

    public DoctorEntity checkIfDoctorExist(String email) {
        DoctorEntity doctor = doctorRepository.findByEmail(email);
        if( Objects.isNull(doctor) ) {
            throw new EntityNotFoundException("Nie ma doktora o takim adresie email");
        }
        return doctor;
    }
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



    public Set<PersonDTO> getPatients(String email) {
        return doctorRepository.getPatientsForDoctor(email)
                .stream()
                .map(PatientEntityMapper::map)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }



    public PersonDTO.DoctorDTO getDoctor(String email) {
        return DoctorEntityMapper.map(checkIfDoctorExist(email));
    }
}
