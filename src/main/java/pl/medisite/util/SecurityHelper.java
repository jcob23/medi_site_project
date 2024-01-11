package pl.medisite.util;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.PersonDTO;
import pl.medisite.service.AppointmentService;
import pl.medisite.service.DoctorService;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SecurityHelper {
    AppointmentService appointmentService;
    DoctorService doctorService;

    public void checkUserAccessToPatientInformation(String email, User user) throws AccessDeniedException {
        String userName = user.getUsername();
        var authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if( authorities.contains("PATIENT") && !email.equals(userName) ) {
            throw new AccessDeniedException("Dostęp do danych innego użytkownika jest zabroniony");
        }
    }

    public void compareUserEmailWithRequestEmail(User user, String requestEmail) throws AccessDeniedException {
        String userEmail = user.getUsername();
        var authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if( !requestEmail.equals(userEmail) && !authorities.contains("ADMIN") ) {
            throw new AccessDeniedException("Dostęp do danych innego użytkownika jest zabroniony");
        }
    }

    public void checkDoctorAccessToPatientInformation(String patientEmail, String doctorEmail) throws AccessDeniedException {
        Set<String> patientEmails = doctorService.getPatients(doctorEmail)
                .stream()
                .map(PersonDTO::getEmail)
                .collect(Collectors.toSet());

        if( !patientEmails.contains(patientEmail) )
            throw new AccessDeniedException("Dostęp do danych obcych pacjentów jest zabroniony");
    }

    public void checkDoctorAccessToAppointmentInformation(Integer appointmentId, String email) throws AccessDeniedException {
        Set<Integer> patientEmails = appointmentService.getDoctorAppointments(email)
                .stream()
                .map(AppointmentDTO::getId)
                .collect(Collectors.toSet());

        if( !patientEmails.contains(appointmentId) )
            throw new AccessDeniedException("Dostęp do wizyt nieprzypisanych pacjentów jest zabroniony");
    }
}
