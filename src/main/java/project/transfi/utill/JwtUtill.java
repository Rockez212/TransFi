package project.transfi.utill;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.transfi.exception.JwtValidateException;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtUtill {
    @Value("${jwt_secret}")
    private String secret;


    public String generateAccessToken(String username) {
        Date expiration = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return Jwts.builder()
                .setSubject("USER TOKEN")
                .claim("username", username)
                .setIssuedAt(new Date())
                .setIssuer("TransFI")
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    public Map<String, String> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .requireIssuer("TransFI")
                    .parseClaimsJws(token)
                    .getBody();

            Map<String, String> result = new HashMap<>();
            claims.forEach((k, v) -> result.put(k, v.toString()));

            return result;
        } catch (Exception e) {
            throw new JwtValidateException("Jwt token validation failed");
        }
    }
}
