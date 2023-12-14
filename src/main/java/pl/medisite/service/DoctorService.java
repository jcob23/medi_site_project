package pl.medisite.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.buisness.DoctorDTO;
import pl.medisite.controller.system.UserDTO;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.security.UserEntity;

@Service
@AllArgsConstructor
public class DoctorService {

    DoctorRepository doctorRepository;
    UserService userService;

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

}
