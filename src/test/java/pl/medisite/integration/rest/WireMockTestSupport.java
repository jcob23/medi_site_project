package pl.medisite.integration.rest;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

public interface WireMockTestSupport {

    default void stubForCountryAirInformation(WireMockServer wireMockServer){
        wireMockServer.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/pjp/all"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type", "application/ld+json")
                        .withBodyFile("wiremock/countryAir.json")
                        .withTransformers("response-template")));
    }
}
