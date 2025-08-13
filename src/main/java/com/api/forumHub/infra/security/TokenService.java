package com.api.forumHub.infra.security;

import com.api.forumHub.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("ForumHub API")
                    .withSubject(user.getEmail())
                    .withClaim("id", user.getId())
                    .withIssuedAt(generateCreationData())
                    //expiresAt -> metodo para expiração do token
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error generating JWT token", exception);
        }
    }

    //validação do token com Auth0
    public String getSubject(String tokenJWT) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("ForumHub API")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception){
            // Invalid signature/claims
            throw new RuntimeException("Invalid or expired JWT token");
        }
    }

    private Instant generateCreationData() {
        return LocalDateTime.now(ZoneId.of("GMT-3")).toInstant(ZoneOffset.of("-03:00"));
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
