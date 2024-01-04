package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.util.DateTimeHelper;

import java.text.ParseException;
import java.time.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private DateTimeHelper dateTimeHelper;

    public Set<AppointmentDTO> getPatientAppointments(String email) {
        return appointmentRepository.getPatientAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentDTO::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getPatientFutureAppointments(String email) {
        return appointmentRepository.getPatientFutureAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentDTO::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getPatientPastAppointments(String email) {
        return appointmentRepository.getPatientPastAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentDTO::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getDoctorAppointments(String email) {
        return appointmentRepository.getDoctorAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentDTO::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
    public Set<AppointmentDTO> getDoctorFutureFreeAppointments(String email) {
        return appointmentRepository
                .getDoctorFutureFreeAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentDTO::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentEntity> getDoctorFutureAppointments(String email) {
        return appointmentRepository.getDoctorFutureAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getDoctorPastAppointments(String email) {
        return appointmentRepository.getDoctorPastAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public AppointmentEntity getById(Integer id) {
        return appointmentRepository.getById(id);

    }

    public AppointmentDTO getAppointment(Integer id) {
        return AppointmentDTO.mapAppointment(appointmentRepository.getById(id));
    }

    @Transactional
    public void deleteAppointment(Integer id) throws BadRequestException {
        AppointmentEntity appointmentEntity = appointmentRepository.getById(id);
        if( appointmentEntity.getAppointmentEnd().isBefore(ZonedDateTime.now()) ) {
            throw new BadRequestException();
        }
        appointmentRepository.deleteAppointment(id);
    }

    public void createNewAppointment(NewAppointmentDTO newAppointmentDTO, String email) throws ParseException {
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email);
        if( newAppointmentDTO.getType().equals("type1") ) {
            createAppointment(newAppointmentDTO.getDate_input(), newAppointmentDTO.getHours_input1(),
                    newAppointmentDTO.getHours_input2(), doctorEntity);
        } else {
            createAppointments(newAppointmentDTO, doctorEntity);
        }
    }

    private void createAppointment(LocalDate date, LocalTime appointmentStart, LocalTime appointmentEnd, DoctorEntity doctorEntity) {
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .appointmentStart(ZonedDateTime.of(date, appointmentStart, ZoneId.of("Europe/Warsaw")))
                .appointmentEnd(ZonedDateTime.of(date, appointmentEnd, ZoneId.of("Europe/Warsaw")))
                .doctor(doctorEntity)
                .build();
        appointmentRepository.saveAndFlush(appointmentEntity);
    }

    @Transactional
    private void createAppointments(NewAppointmentDTO newAppointmentDTO, DoctorEntity doctorEntity) throws ParseException {
        generateAppointments(newAppointmentDTO.getDate_input(),
                newAppointmentDTO.getRange_date_input(),
                newAppointmentDTO.getHours_input1(),
                newAppointmentDTO.getHours_input2(),
                newAppointmentDTO.getBreak_input(),
                newAppointmentDTO.getVisit_time_input(),
                doctorEntity);
    }


    private void generateAppointments(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, String breakTime, String appointmentTime, DoctorEntity doctorEntity) throws ParseException {

        Duration breakTimeInterval = dateTimeHelper.parseDuration(breakTime);
        Duration appointmentTimeInterval = dateTimeHelper.parseDuration(appointmentTime);
        LocalDate currentDate = dateStart;
        while(currentDate.isBefore(dateEnd) || currentDate.isEqual(dateEnd)) {
            LocalTime currentTime = timeStart;
            while(currentTime.isBefore(timeEnd) || currentTime.equals(timeEnd)) {
                createAppointment(currentDate, currentTime, currentTime.plus(appointmentTimeInterval), doctorEntity);
                currentTime = currentTime.plus(appointmentTimeInterval).plus(breakTimeInterval);
                if( currentTime.isAfter(timeEnd) || currentTime.equals(timeEnd) ) {
                    break;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    @Transactional
    public void updateAppointment(Integer appointmentId, String note) {
        AppointmentEntity appointmentEntity = appointmentRepository.getById(appointmentId);
        appointmentEntity.setNote(note);
        appointmentRepository.save(appointmentEntity);
    }

    public Set<DiseaseEntity> getPatientDiseases(Integer appointmentId) {
        return appointmentRepository.getDiseases(appointmentId);
    }

    @Transactional
    public void bookAppointment(Integer appointmentId, String email) {
        AppointmentEntity appointmentEntity = appointmentRepository.getById(appointmentId);
        appointmentEntity.setPatient(patientRepository.findByEmail(email));
        appointmentRepository.saveAndFlush(appointmentEntity);
    }

    public PatientEntity getPatient(Integer appointmentId) {
        return appointmentRepository.getPatientByAppointmentId(appointmentId);
    }
}
