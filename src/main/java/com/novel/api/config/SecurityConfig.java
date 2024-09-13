package com.novel.api.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.config.filter.JsonEmailPasswordLoginFilter;
import com.novel.api.config.filter.JwtFilter;
import com.novel.api.config.filter.handler.LoginFailureHandler;
import com.novel.api.config.filter.handler.LoginSuccessJWTProvideHandler;
import com.novel.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final UserService userService;


    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;
    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/*/users/*", "/login").permitAll()
                        .anyRequest().authenticated())

                .addFilterAt(jsonEmailPasswordLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtFilter(userService, secretKey), JsonEmailPasswordLoginFilter.class)

                .sessionManagement((session) -> session
                        .sessionCreationPolicy(STATELESS))
                .build();
    }


    @Bean
    public JsonEmailPasswordLoginFilter jsonEmailPasswordLoginFilter() {
        JsonEmailPasswordLoginFilter filter = new JsonEmailPasswordLoginFilter(objectMapper);
        filter.setAuthenticationManager(authenticationManager());

        filter.setAuthenticationSuccessHandler(loginSuccessJWTProvideHandler());
        filter.setAuthenticationFailureHandler(loginFailureHandler());

        return filter;
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager() {//AuthenticationManager 등록
        DaoAuthenticationProvider provider = daoAuthenticationProvider();//DaoAuthenticationProvider 사용
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        return daoAuthenticationProvider;
    }

    @Bean
    public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
        return new LoginSuccessJWTProvideHandler(secretKey, expiredTimeMs, objectMapper);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }
}
