package pl.medisite.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediSiteUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getRole());
        return buildUserForAuthentication(user,authority);

    }

    private UserDetails buildUserForAuthentication (UserEntity user, SimpleGrantedAuthority authority) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                List.of(authority)
        );
    }
}
