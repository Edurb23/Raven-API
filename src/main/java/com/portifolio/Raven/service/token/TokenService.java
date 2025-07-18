package com.portifolio.Raven.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.portifolio.Raven.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class TokenService {

    @Value("$(api.token.secret)")
    private String passwordToken;

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(passwordToken);
            return JWT.create()
                    .withIssuer("Raven API")
                    .withSubject(user.getEmail())
                    .withExpiresAt(Instant.now().plus(Duration.ofHours(2)))
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new RuntimeException("Erro ao gerar token jwt");
        }
    }






}
