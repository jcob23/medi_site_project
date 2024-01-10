package pl.medisite.infrastructure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider
    ) throws Exception {
        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    //    obsługa błędu 403
//    https://mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/
    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain filterChainEnabled(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(internetUserPages()).permitAll()
                        .requestMatchers(medisiteUserPages()).hasAnyAuthority("PATIENT", "ADMIN", "DOCTOR")
                        .requestMatchers(patientAndAdminPages()).hasAnyAuthority("PATIENT", "ADMIN")
                        .requestMatchers(adminPages()).hasAuthority("ADMIN")
                        .requestMatchers(patientPages()).hasAuthority("PATIENT")
                        .requestMatchers(doctorPages()).hasAuthority("DOCTOR")
                )
                .formLogin(formLogin -> formLogin.permitAll()
                        .loginPage("/login")
                        .defaultSuccessUrl("/home", true)
                        .failureHandler(customAuthenticationFailureHandler)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }

    private String[] internetUserPages() {
        return new String[]{
                "/login",
                "error",
                "/register/**",
                "/register/save",
                "/forget/**",
                "/api/**",
                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**"
        };
    }

    private String[] medisiteUserPages() {
        return new String[]{
                "/home/**",
                "/img/**"
        };
    }

    private String[] patientAndAdminPages() {
        return new String[]{
                "/patient/appointments/**",
                "/patient/delete_appointment/**",
        };
    }

    private String[] adminPages() {
        return new String[]{"/admin/**"};
    }

    private String[] doctorPages() {
        return new String[]{
                "/doctor",
                "/doctor/update",
                "/doctor/patients",
                "/doctor/patient_diseases",
                "/doctor/add_single_appointment",
                "/doctor/add_multiple_appointments",
                "/doctor/edit_disease/**",
                "/doctor/patient_diseases/**",
                "/doctor/appointments/**",
                "/doctor/patient_appointments/**",
                "/doctor/appointment_details/**",
                "/doctor/update_note/**",
                "/doctor/delete_appointment/**"
        };
    }

    private String[] patientPages() {
        return new String[]{
                "/patient",
                "/patient/update",
                "/patient/doctors",
                "/patient/book_appointment",
                "/patient/book_appointment/**",
                "/patient/diseases/**"};
    }
}
