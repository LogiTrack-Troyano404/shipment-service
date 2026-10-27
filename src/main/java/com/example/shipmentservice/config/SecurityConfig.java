package com.example.shipmentservice.config;

import com.example.shipmentservice.security.JwtTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenValidatorFilter jwtTokenValidatorFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (Swagger y documentación)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Reglas de negocio y control de acceso por roles
                        .requestMatchers(HttpMethod.POST, "/api/v1/envios").hasAnyRole("CLIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/envios/*/cancelar").hasAnyRole("CLIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/v1/envios/*/asignar-repartidor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/envios/**").hasAnyRole("REPARTIDOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/envios/**").hasAnyRole("CLIENTE", "REPARTIDOR", "ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenValidatorFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}