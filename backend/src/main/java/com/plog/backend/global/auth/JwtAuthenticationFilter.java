package com.plog.backend.global.auth;

import com.plog.backend.global.util.JwtTokenUtil;
import com.plog.backend.global.exception.NotValidRequestException;
import com.plog.backend.global.exception.TimeoutException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Getter
@Setter
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final JwtTokenUtil jwtTokenUtil;
    private final PloberUserDetailService userDetailsService;

    // JWT 검증을 건너뛰어야 하는 URI와 HTTP 메소드 목록
    private static final Map<String, Set<String>> EXCLUDE_URLS = new HashMap<>() {{
        put("/api/user", new HashSet<>(List.of("POST"))); // 회원가입 요청 제외
        put("/api/user/email", new HashSet<>(List.of("POST"))); // 이메일 관련 요청 제외
        put("/api/user/email/send", new HashSet<>(List.of("POST")));
        put("/api/user/email/check", new HashSet<>(List.of("POST")));
        put("/api/user/login", new HashSet<>(List.of("POST"))); // 로그인 요청 제외
        put("/api/user/password/**", new HashSet<>(List.of("PATCH", "POST"))); // PATCH와 POST 제외
        put("/api/user/sns/**", new HashSet<>(List.of("POST"))); // SNS 관련 요청 제외
        put("/api/auth/refresh", new HashSet<>(List.of("POST"))); // 토큰 갱신 제외
        put("/api/user/report", new HashSet<>(List.of("POST"))); // 분석 리포트 제외
        put("/api/auth/logout", new HashSet<>(List.of("POST"))); // 로그아웃 제외
    }};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(JwtTokenUtil.HEADER_STRING);

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        log.info("JWT Filter 확인 : URI {}, Method {}, 전체 URL {}", requestURI, requestMethod, request.getRequestURL());

        String userId = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                userId = jwtTokenUtil.getVerifier().verify(jwtToken).getSubject();
                log.info("jwtToken 속 정보 확인 : {}", userId);
            } catch (TimeoutException e) {
                SecurityContextHolder.clearContext();
                log.info("JWT Filter - JWT 토큰이 만료되었습니다.");
                return;
            } catch (NotValidRequestException e) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write(e.getMessage());
                log.info("JWT Filter - JWT 토큰이 유효하지 않습니다.");
                return;
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT Filter - Invalid or expired JWT token");
                return;
            }
        } else {

            if(requestMethod.equals("GET") || requestMethod.equals("OPTIONS")) {
                log.info(">>> JWT Filter 제외 - GET 혹은 OPTIONS 제외");
                chain.doFilter(request, response);
                return;
            }

            // 요청 URI와 메소드가 제외 목록에 포함되어 있는지 확인
            boolean isExcluded = EXCLUDE_URLS.entrySet().stream()
                    .anyMatch(entry -> pathMatcher.match(entry.getKey(), requestURI) && entry.getValue().contains(requestMethod));

            if (isExcluded) {
                log.info(">>> JWT Filter - 제외 목록에서 제외");
                chain.doFilter(request, response); // 필터 체인 계속 진행
                return;
            }

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT Token is missing or does not begin with Bearer String");
            return;
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);

            if (jwtTokenUtil.validateToken(jwtToken)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        try {
            log.info("JWT Filter End");
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception occurred in JwtAuthenticationFilter", e);
            e.printStackTrace();
            throw new ServletException("Exception in JwtAuthenticationFilter", e);
        }
    }
}
