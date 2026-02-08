package com.sv.meeting.api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // TODO EXPLORE THIS
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, JwtAuthFilter jwtAuthFilter) throws Exception {

        httpSecurity.cors(c -> {})
        .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // ✅ REQUIRED for H2 Console (frames)
                .headers(headers ->
                        headers.frameOptions(frame -> frame.disable())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/rooms/**")
                        .authenticated()
                        .requestMatchers("/api/v1/**").authenticated()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults());

        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
