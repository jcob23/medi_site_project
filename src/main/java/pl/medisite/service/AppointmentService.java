package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentDTO;
import pl.medisite.controller.DTO.NewAppointmentsDTO;
import pl.medisite.controller.DTO.Note;
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

    public PatientEntity getPatientByAppointmentId(Integer appointmentId) {
        checkIfAppointmentExist(appointmentId);
        return patientService.getPatientByAppointmentId(appointmentId);
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
            appointments = appointmentRepository.getDoctorAppointments(email, sort);
        } else if( "future".equals(filter) ) {
            appointments = appointmentRepository.getDoctorFutureAppointments(email, sort);
        } else {
            appointments = appointmentRepository.getDoctorPastAppointments(email, sort);
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
    public void deleteAppointment(Integer id) throws BadRequestException {
        AppointmentEntity appointmentEntity = checkIfAppointmentExist(id);
        if( appointmentEntity.getAppointmentEnd().isBefore(ZonedDateTime.now()) ) {
            throw new BadRequestException();
        }
        appointmentRepository.deleteAppointment(id);
    }

    public void createSingleAppointment(NewAppointmentDTO newAppointment, String email) {
        DoctorEntity doctorEntity = doctorService.checkIfDoctorExist(email);
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .appointmentStart(
                        ZonedDateTime.of(
                                newAppointment.getAppointmentDate(),
                                LocalTime.parse(
                                        newAppointment.getAppointmentTimeStart(),
                                        DateTimeFormatter.ISO_TIME),
                                ZoneId.of("Europe/Warsaw")
                        ))
                .appointmentEnd(
                        ZonedDateTime.of(
                                newAppointment.getAppointmentDate(),
                                LocalTime.parse(
                                        newAppointment.getAppointmentTimeEnd(),
                                        DateTimeFormatter.ISO_TIME),
                                ZoneId.of("Europe/Warsaw")
                        ))
                .doctor(doctorEntity)
                .build();
        appointmentRepository.saveAndFlush(appointmentEntity);
    }

    @Transactional
    public void createMultipleAppointments(NewAppointmentsDTO newAppointmentsDTO, String doctorEmail) {
        DoctorEntity doctorEntity = doctorService.checkIfDoctorExist(doctorEmail);

        List<AppointmentEntity> appointmentEntityList = generateAppointments(newAppointmentsDTO.getAppointmentDate(),
                newAppointmentsDTO.getEndDate(),
                LocalTime.parse(newAppointmentsDTO.getAppointmentTimeStart(), DateTimeFormatter.ISO_TIME),
                LocalTime.parse(newAppointmentsDTO.getAppointmentTimeEnd(), DateTimeFormatter.ISO_TIME),
                newAppointmentsDTO.getBreakTime(),
                newAppointmentsDTO.getVisitTime(),
                doctorEntity);
        appointmentRepository.saveAllAndFlush(appointmentEntityList);
    }


    private List<AppointmentEntity> generateAppointments(LocalDate dateStart, LocalDate dateEnd, LocalTime timeStart, LocalTime timeEnd, String breakTime, String appointmentTime, DoctorEntity doctorEntity) {
        List<AppointmentEntity> appointmentEntityList = new ArrayList<>();
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
                appointmentEntityList.add(appointmentEntity);
                currentTime = currentTime.plus(appointmentTimeInterval).plus(breakTimeInterval);
                if( currentTime.isAfter(timeEnd) || currentTime.equals(timeEnd) ) {
                    break;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        return appointmentEntityList;
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
