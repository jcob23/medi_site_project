package pl.medisite.infrastructure.database;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.medisite.infrastructure.security.RoleEntity;
import pl.medisite.infrastructure.security.RoleRepository;

import java.util.List;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleRepositoryJpaTest extends AbstractJpaIT{

    RoleRepository roleRepository;
    @Test
    void roleCanBeFoundCorrectly() {
        List<RoleEntity> all = roleRepository.findAll();
        Assertions.assertThat(all).hasSize(3);
    }

}
