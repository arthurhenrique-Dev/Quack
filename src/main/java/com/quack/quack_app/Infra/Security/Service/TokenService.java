package com.quack.quack_app.Infra.Security.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.quack.quack_app.Domain.Exceptions.ValidationFailedException;
import com.quack.quack_app.Domain.Users.BaseUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Value("valor_que_não_pode_estar_aqui")
    private String secret;

    private Instant expirationDate() {
        return Instant.now().plusSeconds(604800);
    }

    public String generateToken(BaseUser user) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("quack-app")
                    .withSubject(user.getEmail().email())
                    .withClaim("user_id", user.getId().toString())
                    .withClaim("user_role", user.getRole().toString())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTVerificationException exception) {
            var e = new ValidationFailedException("Erro ao gerar JWT");
            log.error(e.getMessage(), exception);
            throw e;
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("quack-app")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    public String getClaim(String token, String claim) {
        return JWT.decode(token).getClaim(claim).asString();
    }
}
