package pl.medisite.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByEmail(String email);

}
