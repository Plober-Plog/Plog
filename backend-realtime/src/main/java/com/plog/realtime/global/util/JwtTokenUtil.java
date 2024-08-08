package com.plog.realtime.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plog.realtime.global.exception.NotValidRequestException;
import com.plog.realtime.global.exception.TimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {
    private static String secretKey;
    private static Integer expirationTime;

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String ISSUER = "plog.com";

    public static final JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    private JwtTokenUtil() {

    }

    public static JwtTokenUtil getInstance() {
        return jwtTokenUtil;
    }

    @Autowired
    JwtTokenUtil(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expiration}") Integer expirationTime) {
        JwtTokenUtil.secretKey = secretKey;
        JwtTokenUtil.expirationTime = expirationTime;
    }

    public JWTVerifier getVerifier() {
        return JWT
                .require(Algorithm.HMAC256(secretKey.getBytes()))
                .withIssuer(ISSUER)
                .build();
    }

    public Long getUserIdFromToken(String token) {
        JWTVerifier verifier = getVerifier();
        try {
            DecodedJWT decodedJWT = verifier.verify(token.replace(TOKEN_PREFIX, ""));
            return Long.parseLong(decodedJWT.getSubject());
        } catch (TokenExpiredException ex) {
            throw new TimeoutException("토큰이 만료되었습니다.");
        } catch (JWTVerificationException ex) {
            throw new NotValidRequestException("유효하지 않은 토큰 입니다. " + ex.getMessage());
        }
    }

    public Date getTokenExpiration(int expirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + expirationTime);
    }

    public Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();
        String[] pairs = json.replaceAll("[{}\"]", "").split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            map.put(keyValue[0].trim(), keyValue[1].trim());
        }
        return map;
    }
}
