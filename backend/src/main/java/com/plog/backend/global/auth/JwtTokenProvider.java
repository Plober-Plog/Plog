package com.plog.backend.global.auth;

import com.plog.backend.domain.user.entity.User;
import com.plog.backend.global.util.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class JwtTokenProvider {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    public String generateAccessToken(Authentication authentication){
        User user = ((PloberUserDetails) authentication.getPrincipal()).getUser();
        return jwtTokenUtil.getToken(user.getUserId());
    }

    public String generateRefreshToken(Authentication authentication){
        User user = ((PloberUserDetails) authentication.getPrincipal()).getUser();
        return jwtTokenUtil.getRefreshToken(user.getUserId());
    }

    public boolean validateToken(String token, HttpServletRequest request){
        boolean isValid = jwtTokenUtil.validateToken(token);
        if (!isValid) {
            request.setAttribute("exception", "INVALID_OR_EXPIRED_JWT");
        }
        return isValid;
    }

    public boolean validateRefreshToken(String token, HttpServletRequest request){
        boolean isValid = jwtTokenUtil.validateRefreshToken(token);
        if (!isValid) {
            request.setAttribute("exception", "INVALID_OR_EXPIRED_REFRESH_JWT");
        }
        return isValid;
    }

    public Authentication getAuthentication(String token) {
        String userId = jwtTokenUtil.getVerifier().verify(token).getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getPayload(String token , HttpServletRequest request){
        if (validateToken(token, request)) {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return jwtTokenUtil.jsonToMap(payload).get("sub").toString();
        }
        return null;
    }
}