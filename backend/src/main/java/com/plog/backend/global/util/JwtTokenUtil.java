package com.plog.backend.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.plog.backend.global.exception.NotValidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
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

    public String getToken(Long userId) {
        Date expires = jwtTokenUtil.getTokenExpiration(expirationTime);
        return JWT.create()
                .withSubject(userId + "")
                .withExpiresAt(expires)
                .withIssuer(ISSUER)
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }

    public String getToken(Instant expires, String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(Date.from(expires))
                .withIssuer(ISSUER)
                .withIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }

    public Date getTokenExpiration(int expirationTime) {
        Date now = new Date();
        return new Date(now.getTime() + expirationTime);
    }

    public boolean validateToken(String token) {
        JWTVerifier verifier = getVerifier();
        try {
            verifier.verify(token.replace(TOKEN_PREFIX, ""));
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
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

    public Long getUserIdFromToken(String token) {
        JWTVerifier verifier = getVerifier();
        try {
            DecodedJWT decodedJWT = verifier.verify(token.replace(TOKEN_PREFIX, ""));
            return Long.parseLong(decodedJWT.getSubject());
        } catch (JWTVerificationException ex) {
            // 검증 실패 시 예외 처리
            throw new NotValidRequestException("JwtTokenUtil : 유요하지 않은 토큰 입니다. " + ex.getMessage());
        }
    }

    public void handleError(String token) {
        JWTVerifier verifier = JWT
                .require(Algorithm.HMAC256(secretKey.getBytes()))
                .withIssuer(ISSUER)
                .build();

        try {
            verifier.verify(token.replace(TOKEN_PREFIX, ""));
        } catch (AlgorithmMismatchException ex) {
            throw ex;
        } catch (InvalidClaimException ex) {
            throw ex;
        } catch (SignatureGenerationException ex) {
            throw ex;
        } catch (SignatureVerificationException ex) {
            throw ex;
        } catch (TokenExpiredException ex) {
            throw ex;
        } catch (JWTCreationException ex) {
            throw ex;
        } catch (JWTDecodeException ex) {
            throw ex;
        } catch (JWTVerificationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
