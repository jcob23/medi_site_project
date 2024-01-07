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
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DiseaseEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.entity.PatientEntity;
import pl.medisite.infrastructure.database.mapper.AppointmentMapper;
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
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository;
    private DateTimeHelper dateTimeHelper;

    public Set<AppointmentDTO> getPatientAppointments(String email) {
        return appointmentRepository.getPatientAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getPatientFutureAppointments(String email) {
        return appointmentRepository.getPatientFutureAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getPatientPastAppointments(String email) {
        return appointmentRepository.getPatientPastAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getDoctorAppointments(String email) {
        return appointmentRepository.getDoctorAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentDTO> getDoctorFutureFreeAppointments(String email) {
        return appointmentRepository
                .getDoctorFutureFreeAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<AppointmentEntity> getDoctorFutureAppointments(String email) {
        return appointmentRepository.getDoctorFutureAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public Set<AppointmentEntity> getDoctorPastAppointments(String email) {
        return appointmentRepository.getDoctorPastAppointments(email, Sort.by(Sort.Direction.ASC, "appointmentStart"));
    }

    public AppointmentEntity getAppointmentById(Integer id) {
        AppointmentEntity appointmentEntity = appointmentRepository.getById(id);
        if( Objects.isNull(appointmentEntity) ) {
            throw new EntityNotFoundException();
        }
        return appointmentEntity;

    }

    public AppointmentDTO getAppointment(Integer id) {
        return AppointmentMapper.mapAppointment(getAppointmentById(id));
    }

    @Transactional
    public void deleteAppointment(Integer id) throws BadRequestException {
        AppointmentEntity appointmentEntity = getAppointmentById(id);
        if( appointmentEntity.getAppointmentEnd().isBefore(ZonedDateTime.now()) ) {
            throw new BadRequestException();
        }
        appointmentRepository.deleteAppointment(id);
    }

    public void createSingleAppointment(NewAppointmentDTO newAppointment, String email) {
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email);
        if( Objects.isNull(doctorEntity) ) {
            throw new EntityNotFoundException();
        }
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
    public void createMultipleAppointments(NewAppointmentsDTO newAppointmentsDTO, String email) {
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email);
        if( Objects.isNull(doctorEntity) ) {
            throw new EntityNotFoundException();
        }

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
                        .appointmentStart(ZonedDateTime.of(currentDate,currentTime,ZoneId.of("Europe/Warsaw")))
                        .appointmentEnd(ZonedDateTime.of(currentDate,currentTime.plus(appointmentTimeInterval),ZoneId.of("Europe/Warsaw")))
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
    public void updateAppointment(Integer appointmentId, String note) {
        AppointmentEntity appointmentEntity = getAppointmentById(appointmentId);
        appointmentEntity.setNote(note);
        appointmentRepository.save(appointmentEntity);
    }

    public Set<DiseaseEntity> getPatientDiseases(Integer appointmentId) {
        return appointmentRepository.getDiseases(appointmentId);
    }

    @Transactional
    public void bookAppointment(Integer appointmentId, String email) {
        AppointmentEntity appointmentEntity = getAppointmentById(appointmentId);
        appointmentEntity.setPatient(patientRepository.findByEmail(email));
        appointmentRepository.saveAndFlush(appointmentEntity);
    }

    public PatientEntity getPatient(Integer appointmentId) {
        return appointmentRepository.getPatientByAppointmentId(appointmentId);
    }

    public Set<AppointmentDTO> getPatientFutureAppointmentsForDoctor(String patientEmail, String doctorEmail) {
        return appointmentRepository.getPatientFutureAppointmentsForDoctor(patientEmail,doctorEmail, Sort.by(Sort.Direction.ASC, "appointmentStart"))
                .stream()
                .map(AppointmentMapper::mapAppointment)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


}
