package pl.medisite.integration.rest;

import io.restassured.http.ContentType;

import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;
import pl.medisite.infrastructure.pjp.AirInformation;

import java.util.List;

public interface PjpControllerTestSupport {
    RequestSpecification requestSpecification();
    default List<AirInformation> pjpCountryInformationInfo(){
        return requestSpecification().get("/api/pjp/all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
//                .contentType(ContentType.JSONLD)
                .extract()
                .body()
                .jsonPath()
                .getList(".",AirInformation.class);


    }
}
