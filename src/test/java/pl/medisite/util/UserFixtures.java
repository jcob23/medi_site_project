package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.controller.DTO.NewUserDTO;
import pl.medisite.infrastructure.security.RoleEntity;
import pl.medisite.infrastructure.security.UserEntity;

@UtilityClass
public class UserFixtures {

    public static NewUserDTO userDTO1 (){
        return NewUserDTO.builder()
                .email("test1@email.com")
                .password("12345")
                .build();
    }

    public static NewUserDTO userDTO2 (){
        return NewUserDTO.builder()
                .email("test2@email.com")
                .password("12345")
                .build();
    }

    public static NewUserDTO userDTO3 (){
        return NewUserDTO.builder()
                .email("test3@email.com")
                .password("12345")
                .build();
    }

    public static UserEntity userEntity1 (){
        return UserEntity.builder()
                .id(2)
                .email("test1@email.com")
                .password("12345")
                .role(new RoleEntity("PATIENT"))
                .build();
    }

    public static UserEntity userEntity2 (){
        return UserEntity.builder()
                .id(3)
                .email("test2@email.com")
                .password("12345")
                .role(new RoleEntity("PATIENT"))
                .build();
    }

    public static UserEntity userEntity3 (){
        return UserEntity.builder()
                .id(4)
                .email("test3@email.com")
                .password("12345")
                .role(new RoleEntity("PATIENT"))
                .build();
    }
}
