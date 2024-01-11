package pl.medisite.integration.rest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.medisite.infrastructure.pjp.AirInformation;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PjpRestControllerTest extends RestAssuredIntegrationTestBase
        implements WireMockTestSupport, PjpControllerTestSupport {

//    @Test
//    void wireMockTestForPjpApi() {
//
//        stubForCountryAirInformation(wireMockServer);
//        //Given when
//        List<AirInformation> airInformation = pjpCountryInformationInfo();
//
//        //then
//        Assertions.assertThat(airInformation.size()).isEqualTo(20);
//    }
}
