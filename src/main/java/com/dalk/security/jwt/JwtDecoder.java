package com.dalk.security.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dalk.domain.User;
import com.dalk.security.UserDetailsImpl;
import com.dalk.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

import static com.dalk.security.jwt.JwtTokenUtils.*;


@Component
public class JwtDecoder {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    public JwtDecoder(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    public User getAuthenticationUser(String token) {
        UserDetailsImpl userDetails = userDetailsServiceImpl.loadUserById(Long.parseLong(token));
        return userDetails.getUser();
    }

    public String decodeUsername(String token) {
        System.out.println(token);
        DecodedJWT decodedJWT = isValidToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효한 토큰이 아닙니다 1."));

        Date expiredDate = decodedJWT
                .getClaim(CLAIM_EXPIRED_DATE)
                .asDate();

        Date now = new Date();
        if (expiredDate.before(now)) {
            System.out.println(token);
            throw new IllegalArgumentException("유효한 토큰이 아닙니다 날짜가 지남.");
        }

        String username = decodedJWT
                .getClaim(CLAIM_USER_NAME)
                .asString();

        System.out.println("토큰검증 : " + username);
        return username;
    }

    private Optional<DecodedJWT> isValidToken(String token) {
        DecodedJWT jwt = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT
                    .require(algorithm)
                    .build();

            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return Optional.ofNullable(jwt);
    }
}