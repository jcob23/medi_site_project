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
            HttpSecurity httpSecurity,
            AuthenticationProvider authenticationProvider
    ) throws Exception {
        return httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

    // obsługa błędu 403
    //https://mkyong.com/spring-security/customize-http-403-access-denied-page-in-spring-security/
    @Bean
    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "true", matchIfMissing = true)
    public SecurityFilterChain filterChainEnabled(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/login", "/register/**", "/register/save", "/forget/**").permitAll()
                        .requestMatchers("/home/**", "/patient/diseases/**").hasAnyAuthority("PATIENT", "ADMIN", "DOCTOR")
                        .requestMatchers("/patient/appointments/**","/patient/delete_appointment/**").hasAnyAuthority("PATIENT", "ADMIN")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/patient", "/patient/doctors").hasAuthority("PATIENT")
                        .requestMatchers("/doctor/**","/doctor/add_appointment").hasAuthority("DOCTOR")
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

//    @Bean
//    @ConditionalOnProperty(value = "spring.security.enabled", havingValue = "false")
//    public SecurityFilterChain filterChainDisabled(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(request -> request
//                        .anyRequest()
//                        .permitAll()
//                )
//                .build();
//    }

}
