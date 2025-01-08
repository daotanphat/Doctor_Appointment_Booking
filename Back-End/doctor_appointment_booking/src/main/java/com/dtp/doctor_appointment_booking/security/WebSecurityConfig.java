package com.dtp.doctor_appointment_booking.security;

import com.dtp.doctor_appointment_booking.security.jwt.AuthEntryPointJwt;
import com.dtp.doctor_appointment_booking.security.jwt.AuthTokenFilter;
import com.dtp.doctor_appointment_booking.security.services.UserDetailsServiceImpl;
import com.dtp.doctor_appointment_booking.service.EndPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        {
                            auth.requestMatchers(HttpMethod.GET, EndPointService.publicGetEndPoint).permitAll();
                            auth.requestMatchers(HttpMethod.POST, EndPointService.publicPostEndPoint).permitAll();
                            auth.requestMatchers(HttpMethod.GET, EndPointService.adminGetEndPoint).hasAuthority("ADMIN");
                            auth.requestMatchers(HttpMethod.POST, EndPointService.adminPostEndPoint).hasAuthority("ADMIN");
                            auth.requestMatchers(HttpMethod.GET, EndPointService.userGetEndPointAuth).hasAuthority("USER");
                            auth.requestMatchers(HttpMethod.POST, EndPointService.userPostEndPointAuth).hasAuthority("USER");
                            auth.requestMatchers(HttpMethod.GET, EndPointService.userGetEndPoint).permitAll();
                            auth.requestMatchers(HttpMethod.GET, EndPointService.appointmentGetEndPointAuth).hasAnyAuthority("USER", "DOCTOR");
                            auth.requestMatchers(HttpMethod.POST, EndPointService.appointmentPostEndPointAuth).hasAnyAuthority("USER", "DOCTOR");
                            auth.requestMatchers(HttpMethod.POST, EndPointService.doctorBusyPostEndPointAuth).hasAuthority("DOCTOR");
                        }
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }
}
