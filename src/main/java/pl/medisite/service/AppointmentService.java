package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.buisness.DoctorAppointmentDTO;
import pl.medisite.infrastructure.database.entity.AppointmentEntity;
import pl.medisite.infrastructure.database.entity.DoctorEntity;
import pl.medisite.infrastructure.database.repository.AppointmentRepository;
import pl.medisite.infrastructure.database.repository.DoctorRepository;
import pl.medisite.util.DateHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentService {

    private AppointmentRepository appointmentRepository;
    private DoctorRepository doctorRepository;
    private DateHelper dateHelper;

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

    public void createNewAppointment(DoctorAppointmentDTO doctorAppointmentDTO, String email) throws ParseException {
        DoctorEntity doctorEntity = doctorRepository.findByEmail(email);
        if(doctorAppointmentDTO.getType().equals("type1")){
            createAppointment(doctorAppointmentDTO.getDate_input(),doctorAppointmentDTO.getHours_input1(),
                    doctorAppointmentDTO.getHours_input2(),doctorEntity);
        }else {
            createAppointments(doctorAppointmentDTO, doctorEntity);
        }
    }

    private void createAppointment(String date, String appointmentStart, String appointmentEnd, DoctorEntity doctorEntity){
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .appointmentStart(dateHelper.createZonedDateTime(date, appointmentStart))
                .appointmentEnd(dateHelper.createZonedDateTime(date, appointmentEnd))
                .doctor(doctorEntity)
                .build();
        appointmentRepository.saveAndFlush(appointmentEntity);
    }

    @Transactional
    private void createAppointments(DoctorAppointmentDTO doctorAppointmentDTO, DoctorEntity doctorEntity) throws ParseException {
        generateAppointments(doctorAppointmentDTO.getDate_input(),
                doctorAppointmentDTO.getRange_date_input(),
                doctorAppointmentDTO.getHours_input1(),
                doctorAppointmentDTO.getHours_input2(),
                doctorAppointmentDTO.getBreak_input(),
                doctorAppointmentDTO.getVisit_time_input(),
                doctorEntity);
    }


    private void generateAppointments(String dateStart, String dateEnd, String timeStart, String timeEnd, String breakTime, String appointmentTime, DoctorEntity doctorEntity) throws ParseException {

        LocalDate startDate = LocalDate.parse(dateStart);
        LocalDate endDate = LocalDate.parse(dateEnd);
        LocalTime startTime = LocalTime.parse(timeStart);
        LocalTime endTime = LocalTime.parse(timeEnd);
        Duration breakTimeInterval = dateHelper.parseDuration(breakTime);
        Duration appointmentTimeInterval = dateHelper.parseDuration(appointmentTime);

        LocalDate currentDate = startDate;
        while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
            LocalTime currentTime = startTime;
            while (currentTime.isBefore(endTime) || currentTime.equals(endTime)) {
                createAppointment(currentDate.toString(), currentTime.toString(), currentTime.plus(appointmentTimeInterval).toString(), doctorEntity);
                currentTime = currentTime.plus(appointmentTimeInterval).plus(breakTimeInterval);
                if (currentTime.isAfter(endTime) || currentTime.equals(endTime)) {
                    break;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
    }



}
