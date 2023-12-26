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

    public Set<AppointmentEntity> getPatientAppointments(String email) {
        return appointmentRepository.getPatientAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getPatientFutureAppointments(String email) {
        return appointmentRepository.getPatientFutureAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getPatientPastAppointments(String email) {
        return appointmentRepository.getPatientPastAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getDoctorAppointments(String email) {
        return appointmentRepository.getDoctorAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getDoctorFutureAppointments(String email) {
        return appointmentRepository.getDoctorFutureAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getDoctorPastAppointments(String email) {
        return appointmentRepository.getDoctorPastAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public AppointmentEntity getById(Integer id){
        return appointmentRepository.getById(id);

    }
    @Transactional
    public void deleteAppointment( Integer id) throws BadRequestException {
        AppointmentEntity appointmentEntity = appointmentRepository.getById(id);
        if( appointmentEntity.getAppointmentEnd().isBefore(ZonedDateTime.now()) ) {
            throw new BadRequestException();
        }
        appointmentRepository.deleteAppointment(id);
    }
}
