package pl.medisite.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByEmail(String email);

    void deleteByEmail (String email);

    @Query("SELECT u FROM UserEntity u LEFT JOIN u.token t WHERE t.uuid = :token")
    UserEntity findByToken(@Param("token") UUID token);

}
