package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentsDTO;
import pl.medisite.controller.DTO.Note;
import pl.medisite.exception.AppointmentCancellationException;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.AppointmentEntityMapper;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.infrastructure.database.repository.PatientRepository;
import pl.medisite.util.DateTimeHelper;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DateTimeHelper dateTimeHelper;

    private PatientService patientService;
    private DoctorService doctorService;


    public AppointmentEntity checkIfAppointmentExist(Integer appointmentId) {
        AppointmentEntity appointmentEntity = appointmentRepository.getByAppointmentId(appointmentId);
        if( Objects.isNull(appointmentEntity) ) {
            throw new EntityNotFoundException("Nie istnieje taka wizyta");
        }
        return appointmentEntity;
    }

    public AppointmentDTO getAppointment(Integer id) {
        return AppointmentEntityMapper.mapAppointment(checkIfAppointmentExist(id));
    }
    public Set<AppointmentDTO> getPatientAppointments(String email, String filter) {
        patientService.checkIfPatientExist(email);
        Set<AppointmentEntity> appointments;
        Sort sort = Sort.by(Sort.Direction.ASC, "appointmentStart");
        if( "past".equals(filter) ) {
            appointments = appointmentRepository.getPatientPastAppointments(email, sort);
        } else if( "future".equals(filter) ) {
            appointments = appointmentRepository.getPatientFutureAppointments(email, sort);
        } else {
            appointments = appointmentRepository.getPatientAppointments(email, sort);
        }
        return appointments
                .stream()
                .map(AppointmentEntityMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getDoctorAppointments(String email, String filter) {
        doctorService.checkIfDoctorExist(email);
        Set<AppointmentEntity> appointments;
        Sort sort = Sort.by(Sort.Direction.ASC, "appointmentStart");
        if( "past".equals(filter) ) {
            appointments = appointmentRepository.getDoctorPastAppointments(email, sort);
        } else if( "future".equals(filter) ) {
            appointments = appointmentRepository.getDoctorFutureAppointments(email, sort);
        } else {
            appointments = appointmentRepository.getDoctorAppointments(email, sort);
        }
        return appointments
                .stream()
                .map(AppointmentEntityMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getDoctorFutureFreeAppointments(String email) {
        doctorService.checkIfDoctorExist(email);
        return appointmentRepository
                .getDoctorFutureFreeAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentEntityMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    @Transactional
    public void deleteAppointment(Integer id) {
        AppointmentEntity appointmentEntity = checkIfAppointmentExist(id);
        if( appointmentEntity.getAppointmentEnd().isBefore(ZonedDateTime.now()) ) {
            throw new AppointmentCancellationException("Nie można odwołać wizyty która się już odbyła");
        }
        appointmentRepository.deleteAppointment(id);
    }

    public void createSingleAppointment(NewAppointmentDTO newAppointment, String email) throws BindException {
        LocalTime timeStart = LocalTime.parse(newAppointment.getAppointmentTimeStart(), DateTimeFormatter.ISO_TIME);
        LocalTime timeEnd = LocalTime.parse(newAppointment.getAppointmentTimeEnd(), DateTimeFormatter.ISO_TIME);
        dateTimeHelper.checkIfAppointmentTimeIsValid(timeStart,timeEnd);

        DoctorEntity doctorEntity = doctorService.checkIfDoctorExist(email);
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .appointmentStart(
                        ZonedDateTime.of(newAppointment.getAppointmentDate(), timeStart, ZoneId.of("Europe/Warsaw")))
                .appointmentEnd(
                        ZonedDateTime.of(newAppointment.getAppointmentDate(),timeEnd, ZoneId.of("Europe/Warsaw")))
                .doctor(doctorEntity)
                .build();
        Set<AppointmentEntity> doctorAppointments = appointmentRepository.getDoctorAppointments(email, null);
        dateTimeHelper.checkIfDateIsAvailable(appointmentEntity,doctorAppointments);
        appointmentRepository.saveAndFlush(appointmentEntity);
    }

    @Transactional
    public void createMultipleAppointments(NewAppointmentsDTO newAppointmentsDTO, String doctorEmail) throws BindException {
        DoctorEntity doctorEntity = doctorService.checkIfDoctorExist(doctorEmail);

        Set<AppointmentEntity> appointmentEntityList = generateAppointments(newAppointmentsDTO.getAppointmentDate(),
                newAppointmentsDTO.getEndDate(),
                LocalTime.parse(newAppointmentsDTO.getAppointmentTimeStart(), DateTimeFormatter.ISO_TIME),
                LocalTime.parse(newAppointmentsDTO.getAppointmentTimeEnd(), DateTimeFormatter.ISO_TIME),
                newAppointmentsDTO.getBreakTime(),
                newAppointmentsDTO.getVisitTime(),
                doctorEntity);

        Set<AppointmentEntity> doctorAppointments = appointmentRepository.getDoctorAppointments(doctorEmail, null);
        for(AppointmentEntity appointment : appointmentEntityList) {
            dateTimeHelper.checkIfDateIsAvailable(appointment, doctorAppointments);
        }
        appointmentRepository.saveAllAndFlush(appointmentEntityList);
    }


    private Set<AppointmentEntity> generateAppointments(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, String breakTime, String appointmentTime, DoctorEntity doctorEntity) throws BindException {
        Set<AppointmentEntity> appointmentEntitySet = new HashSet<>();
        Duration breakTimeInterval = dateTimeHelper.parseDuration(breakTime);
        Duration appointmentTimeInterval = dateTimeHelper.parseDuration(appointmentTime);
        LocalDate currentDate = dateStart;
        while(currentDate.isBefore(dateEnd) || currentDate.isEqual(dateEnd)) {
            LocalTime currentTime = timeStart;
            while(currentTime.isBefore(timeEnd) || currentTime.equals(timeEnd)) {
                AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                        .appointmentStart(ZonedDateTime.of(currentDate, currentTime, ZoneId.of("Europe/Warsaw")))
                        .appointmentEnd(ZonedDateTime.of(currentDate, currentTime.plus(appointmentTimeInterval), ZoneId.of("Europe/Warsaw")))
                        .doctor(doctorEntity)
                        .build();
                dateTimeHelper.checkIfAppointmentTimeIsValid(currentTime,currentTime.plus(appointmentTimeInterval));
                appointmentEntitySet.add(appointmentEntity);
                currentTime = currentTime.plus(appointmentTimeInterval).plus(breakTimeInterval);
                if( currentTime.isAfter(timeEnd) || currentTime.equals(timeEnd) ) {
                    break;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        return appointmentEntitySet;
    }

    @Transactional
    public void updateAppointment(Integer appointmentId, Note note) {
        AppointmentEntity appointmentEntity = checkIfAppointmentExist(appointmentId);
        appointmentEntity.setNote(note.getText());
        appointmentRepository.save(appointmentEntity);
    }


    @Transactional
    public void bookAppointment(Integer appointmentId, String email) {
        AppointmentEntity appointmentEntity = checkIfAppointmentExist(appointmentId);
        appointmentEntity.setPatient(patientService.checkIfPatientExist(email));
        appointmentRepository.saveAndFlush(appointmentEntity);
    }


    public Set<AppointmentDTO> getPatientFutureAppointmentsForDoctor(String patientEmail, String doctorEmail) {
        patientService.checkIfPatientExist(patientEmail);
        doctorService.checkIfDoctorExist(doctorEmail);
        return appointmentRepository.getPatientFutureAppointmentsForDoctor(patientEmail, doctorEmail, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentEntityMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


}
