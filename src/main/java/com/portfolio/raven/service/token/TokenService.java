package com.portfolio.raven.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.portfolio.raven.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    @Value("$(api.token.secret)")
    private String passwordToken;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(passwordToken);
            return JWT.create()
                    .withIssuer("raven-api")
                    .withSubject(user.getEmail())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating token");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(passwordToken);
            return JWT.require(algorithm)
                    .withIssuer("raven-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }






}
