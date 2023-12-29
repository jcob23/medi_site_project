package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.buisness.AppointmentDTO;
import pl.medisite.controller.buisness.DoctorDTO;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.security.UserEntity;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService {

    DoctorRepository doctorRepository;
    UserService userService;
    AppointmentService appointmentService;

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
}
