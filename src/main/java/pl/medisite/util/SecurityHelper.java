package pl.medisite.util;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import pl.medisite.controller.DTO.PersonDTO;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SecurityHelper {
    public void checkAuthority(String email, User user) {
        String userName = user.getUsername();
        var authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if( authorities.contains("PATIENT") && !email.equals(userName) ) {
            throw new AccessDeniedException("Dostęp do danych innego użytkownika jest zabroniony");
        }
    }

    public void checkDoctorAccessToPatientInformation(String patientEmail, Set<PersonDTO> patients) {
        Set<String> patientEmails = patients.stream().map(PersonDTO::getEmail).collect(Collectors.toSet());
        if(!patientEmails.contains(patientEmail))
            throw new AccessDeniedException("Dostęp do danych obcych pacjentów jest zabroniony");



    }


}
