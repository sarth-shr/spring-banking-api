package com.example.foneproject.security;

import com.example.foneproject.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${credentials.admin.authorities}")
    private String ADMIN_AUTHORITIES;

    @Value("${credentials.customer.authorities}")
    private String CUSTOMER_AUTHORITIES;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/error/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/customers", "/api/v1/jwt").permitAll()
                                .requestMatchers(HttpMethod.DELETE).hasAuthority(ADMIN_AUTHORITIES)
                                .requestMatchers("/api/v1/admin/**").hasAuthority(ADMIN_AUTHORITIES)
                                .requestMatchers(HttpMethod.POST, "api/v1/accounts/**").hasAuthority(CUSTOMER_AUTHORITIES)
                                .requestMatchers(HttpMethod.GET, "/api/v1/customers", "/api/v1/accounts", "/api/v1/transactions").hasAuthority(ADMIN_AUTHORITIES)
                                .requestMatchers(HttpMethod.GET, "/api/v1/transactions/**", "api/v1/customers/**", "/api/v1/accounts/**").hasAnyAuthority(CUSTOMER_AUTHORITIES, ADMIN_AUTHORITIES)
                                .requestMatchers(HttpMethod.PUT, "/api/v1/customers/**").hasAnyAuthority(CUSTOMER_AUTHORITIES, ADMIN_AUTHORITIES)
                                .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
