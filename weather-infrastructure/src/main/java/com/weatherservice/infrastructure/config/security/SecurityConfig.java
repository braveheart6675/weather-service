package com.weatherservice.infrastructure.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // همه endpointها مجاز (برای development)
                        .anyRequest().permitAll()
                )
                // غیرفعال کردن فرم login
                .formLogin(AbstractHttpConfigurer::disable)
                // غیرفعال کردن basic auth
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }
}