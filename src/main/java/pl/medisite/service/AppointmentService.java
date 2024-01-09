package pl.medisite.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import pl.medisite.controller.DTO.*;
import pl.medisite.exception.AppointmentCancellationException;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.mapper.AppointmentEntityMapper;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
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

    public List<AppointmentDTO> getPatientAppointments(String email){
        return appointmentRepository.getPatientAppointments(email).stream()
                .map(AppointmentEntityMapper::mapAppointment).collect(Collectors.toList());
    }

    public AbstractMap.SimpleEntry<Integer, List<AppointmentDTO>> getPatientAppointments(String email, String filter, PageRequest pageable) {
        patientService.checkIfPatientExist(email);
        Page<AppointmentEntity> appointments;
        if( "past".equals(filter) ) {
            appointments = appointmentRepository.getPatientPastAppointments(email,  pageable);
        } else if( "future".equals(filter) ) {
            appointments = appointmentRepository.getPatientFutureAppointments(email,  pageable);
        } else  {
            appointments = appointmentRepository.getPatientAppointments(email,  pageable);
        }
        Page<AppointmentDTO> mappedAppointments = appointments.map(AppointmentEntityMapper::mapAppointment);
        return new AbstractMap.SimpleEntry<>(mappedAppointments.getTotalPages(),mappedAppointments.getContent());
    }

    public List<AppointmentDTO> getDoctorAppointments(String email){
        return appointmentRepository.getDoctorAppointments(email).stream()
                .map(AppointmentEntityMapper::mapAppointment).collect(Collectors.toList());
    }

    public AbstractMap.SimpleEntry<Integer, List<AppointmentDTO>> getDoctorAppointments(String email, String filter,PageRequest pageable) {
        doctorService.checkIfDoctorExist(email);
        Page<AppointmentEntity> appointments;
        if( "past".equals(filter) ) {
            appointments = appointmentRepository.getDoctorPastAppointments(email,  pageable);
        } else if( "future".equals(filter) ) {
            appointments = appointmentRepository.getDoctorFutureAppointments(email,  pageable);
        } else  {
            appointments = appointmentRepository.getDoctorAppointments(email,  pageable);
        }
        Page<AppointmentDTO> mappedAppointments = appointments.map(AppointmentEntityMapper::mapAppointment);
        return new AbstractMap.SimpleEntry<>(mappedAppointments.getTotalPages(),mappedAppointments.getContent());
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
        Set<AppointmentEntity> doctorAppointments = appointmentRepository.getDoctorAppointments(email);
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

        Set<AppointmentEntity> doctorAppointments = appointmentRepository.getDoctorAppointments(doctorEmail);
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
