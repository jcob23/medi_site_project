package pl.medisite.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SecurityHelper {

    public void checkAuthority(String email, Authentication authentication ){
        String userName = ((User) authentication.getPrincipal()).getUsername();
        var authorities = ((User) authentication.getPrincipal()).getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        if( authorities.contains("PATIENT") && !email.equals(userName) ) {
            throw new AccessDeniedException("Dostęp do danych innego użytkownika jest zabroniony");
        }

    }
}
