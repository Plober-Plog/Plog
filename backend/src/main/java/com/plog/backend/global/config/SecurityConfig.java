package com.plog.backend.global.config;

import com.plog.backend.global.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/user").permitAll() // 회원가입은 JWT 없이 접근 가능
//                        .requestMatchers("/api/admin/**").authenticated() // JWT 필요
//                        .requestMatchers(HttpMethod.PATCH, "/api/user/**").authenticated() // 수정 JWT 필요
//                        .requestMatchers(HttpMethod.DELETE, "/api/user/**").authenticated() // 삭제 JWT 필요
//                        .requestMatchers(HttpMethod.GET, "/api/user").authenticated() // 회원 정보 조회 JWT 필요
//                        .requestMatchers(HttpMethod.GET, "/api/user/plant/**/share").authenticated() // 식물 프로필 공유 JWT 필요
//                        .requestMatchers(HttpMethod.GET, "/api/user/sns/**/comment").authenticated() // 댓글 조회 JWT 필요
//                        .requestMatchers(HttpMethod.POST, "/api/user/password").authenticated() // 현재 비밀번호 확인 JWT 필요
//                        .requestMatchers(HttpMethod.POST, "/api/user/plant/**").authenticated() // 식물 등록 JWT 필요
//                        .requestMatchers(HttpMethod.POST, "/api/user/diary/**").authenticated() // 식물 일지 등록 JWT 필요
//                        .requestMatchers("/api/user/**").permitAll() // JWT 없이 접근 가능
//                        .anyRequest().authenticated() // 나머지 요청은 JWT 필요
                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
