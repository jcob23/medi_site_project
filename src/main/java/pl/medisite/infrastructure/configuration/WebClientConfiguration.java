package pl.medisite.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MimeType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import pl.medisite.infrastructure.pjp_api.ApiClient;
import pl.medisite.infrastructure.pjp_api.api.AgregatyPomiarwApi;
import pl.medisite.infrastructure.pjp_api.api.InformacjeOPrzekroczeniuApi;

import java.util.Collections;

@Configuration
public class WebClientConfiguration {

    @Value("${api.pjp_api.url}")
    private String pjp_apiUrl;

    @Bean
    public WebClient webClient(ObjectMapper objectMapper) {
        final var strategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> {
                    configurer
                            .defaultCodecs()
                            .jackson2JsonEncoder(
                                    new Jackson2JsonEncoder(objectMapper, MediaType.valueOf("application/ld+json")));
                    configurer
                            .defaultCodecs()
                            .jackson2JsonDecoder(
                                    new Jackson2JsonDecoder(objectMapper, MediaType.valueOf("application/ld+json")));
                }).build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .build();
    }

    @Bean
    public MappingJackson2HttpMessageConverter jsonLdHttpMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.valueOf("application/ld+json")));
        return converter;
    }

    @Bean
    public ApiClient apiClient(final WebClient webClient) {
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(pjp_apiUrl);
        return apiClient;
    }

    @Bean
    public AgregatyPomiarwApi agregatyPomiarwApi(final ApiClient apiClient) {
        return new AgregatyPomiarwApi(apiClient);
    }
}
