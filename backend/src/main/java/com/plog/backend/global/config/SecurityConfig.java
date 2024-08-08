package com.plog.backend.global.config;

import com.plog.backend.global.auth.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
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
                .headers(headers -> headers
                        .addHeaderWriter((request, response) -> {
                            response.addHeader("Access-Control-Allow-Origin", "*");
                            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                            response.addHeader("Access-Control-Allow-Headers", "authorization, content-type, xsrf-token, accessToken, refreshToken, userData");
                            response.addHeader("Access-Control-Expose-Headers", "xsrf-token");
                            response.addHeader("Access-Control-Allow-Credentials", "true");
                            if (request.getMethod().equals("OPTIONS")) {
                                response.setStatus(HttpServletResponse.SC_OK);
                            }
                        })
                )

//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addAllowedOrigin("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
