package pl.medisite.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @Email
    private String email;

    @Size(min = 4)
    private String password;


    public Map<String,String> asMap(){
        Map<String, String > result = new HashMap<>();
        Optional.ofNullable(email).ifPresent(value -> result.put("email",email));
        Optional.ofNullable(password).ifPresent(value -> result.put("password",password));
        return result;
    }
}
