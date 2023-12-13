package pl.medisite.controller.system;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotBlank
    @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
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
