package pl.medisite.integration.rest;

import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import pl.medisite.controller.DTO.AppointmentDTO;
import pl.medisite.controller.DTO.DiseaseDTO;
import pl.medisite.controller.DTO.PersonDTO;

public interface PatientControllerTestSupport {
    RequestSpecification requestSpecification();
    default PersonDTO.DoctorDTO listDoctors(){
        return requestSpecification()
                .get("/api/patient/doctors")
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(PersonDTO.DoctorDTO.class);

    }


    default AppointmentDTO getAllPatientAppointments(String email){
        return requestSpecification()
                .get("/api/patient/appointments/" +email)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(AppointmentDTO.class);
    }
    default DiseaseDTO getAllPatientDiseases(String email){
        return requestSpecification()
                .get("/api/patient/diseases/" +email)
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .extract()
                .as(DiseaseDTO.class);
    }

    default void bookAppointment(Integer appointmentId, String userEmail ){
        requestSpecification()
                .param("userEmail", userEmail)
                .patch("/api/patient/book_appointment/" + appointmentId)
                .then()
                .statusCode(HttpStatus.OK.value());


    }
}
