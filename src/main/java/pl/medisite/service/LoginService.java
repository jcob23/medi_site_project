package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.medisite.controller.UserDTO;
import pl.medisite.infrastructure.security.RoleEntity;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class LoginService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    public void saveUser (UserDTO user) {
        UserEntity userEntity = UserEntity.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(roleRepository.getRoleById(2))
                .build();

        userRepository.save(userEntity);
    }
}
