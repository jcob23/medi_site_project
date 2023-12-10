package pl.medisite.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.medisite.controller.UserDTO;
import pl.medisite.infrastructure.security.RoleRepository;
import pl.medisite.infrastructure.security.UserEntity;
import pl.medisite.infrastructure.security.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;

    @Transactional
    public void saveUser (UserDTO user) {
        UserEntity userEntity = UserEntity.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(roleRepository.getRoleById(2))
                .build();

        userRepository.save(userEntity);
    }

    @Transactional
    public List<String> getUsersEmails () {
        return userRepository.findAll().stream().filter(user -> user.getRole().getRole().equals("USER")).map(UserEntity::getEmail).toList();
    }

    @Transactional
    public void deleteUser (String email) {
        userRepository.deleteByEmail(email);
    }
}
