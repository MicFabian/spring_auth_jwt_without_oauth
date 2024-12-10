package de.esteemo.estimo.modules.users.infrastructure;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtConfig jwtConfig;

    public String generateToken(String username, List<String> roles) {
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", String.join(",", roles)) // Add roles as a comma-separated string
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                .sign(Algorithm.HMAC256(jwtConfig.getSecret()));
    }

    public DecodedJWT validateToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(jwtConfig.getSecret()))
                .build()
                .verify(token);
    }

    public String extractUsername(String token) throws JWTVerificationException {
        return validateToken(token).getSubject();
    }

    public List<String> extractRoles(String token) throws JWTVerificationException {
        String roles = validateToken(token).getClaim("roles").asString();
        return List.of(roles.split(","));
    }
}
