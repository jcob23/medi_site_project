package pl.medisite.infrastructure.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;


@Data
@Entity
@NoArgsConstructor
public class PersonInformation {

    @Id
    String email;

    String name;

    String surname;

    String role;

    String phone;


}
