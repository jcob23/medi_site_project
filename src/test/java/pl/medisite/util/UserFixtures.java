package pl.medisite.util;

import lombok.experimental.UtilityClass;
import pl.medisite.controller.DTO.NewUserDTO;
import pl.medisite.infrastructure.security.RoleEntity;
import pl.medisite.infrastructure.security.UserEntity;

@UtilityClass
public class UserFixtures {



    public static UserEntity userEntity1 (){
        return UserEntity.builder()
                .email("test1@email.com")
                .password("12345")
                .role(new RoleEntity(2,"PATIENT"))
                .build();
    }

    public static UserEntity userEntity2 (){
        return UserEntity.builder()
                .email("test2@email.com")
                .password("12345")
                .role(new RoleEntity(2,"PATIENT"))
                .build();
    }

    public static UserEntity userEntity3 (){
        return UserEntity.builder()
                .email("test3@email.com")
                .password("12345")
                .role(new RoleEntity(2,"PATIENT"))
                .build();
    }

    public static UserEntity userEntity4 (){
        return UserEntity.builder()
                .email("test4@email.com")
                .password("12345")
                .role(new RoleEntity(3,"DOCTOR"))
                .build();
    }

    public static UserEntity userEntity5 (){
        return UserEntity.builder()
                .email("test5@email.com")
                .password("12345")
                .role(new RoleEntity(3,"DOCTOR"))
                .build();
    }

    public static UserEntity userEntity6 (){
        return UserEntity.builder()
                .email("test6@email.com")
                .password("12345")
                .role(new RoleEntity(3,"DOCTOR"))
                .build();
    }
}
