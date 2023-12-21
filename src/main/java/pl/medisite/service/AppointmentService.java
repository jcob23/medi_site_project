package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;

import java.time.ZonedDateTime;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    public Set<AppointmentEntity> getAppointments(String email){
        return appointmentRepository.getAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }
    public Set<AppointmentEntity> getFutureAppointments(String email){
        return appointmentRepository.getFutureAppointments(email,Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }
    public Set<AppointmentEntity> getPastAppointments(String email){
        return appointmentRepository.getPastAppointments(email,Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }
    @Transactional
    public void deleteAppointment(String email, Integer id) throws BadRequestException {
        AppointmentEntity appointmentEntity = appointmentRepository.getById(id);
        if(appointmentEntity.getAppointmentEnd().isBefore(ZonedDateTime.now())){
            throw new BadRequestException();
        }
        appointmentRepository.deleteAppointment(email,id);
    }
}
