package dev.alexandrevieira.sales.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
@Slf4j
public class JwtService {
    @Value("${security.jwt.expiration}")
    private String expiration;

    @Value("${security.jwt.signature}")
    private String signature;

    public Claims getClaims(String token) throws ExpiredJwtException {
        log.debug(this.getClass().getSimpleName() + ".getClaims(String token)");
        return Jwts
                .parser()
                .setSigningKey(signature)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean tokenIsValid(String token) {
        log.debug(this.getClass().getSimpleName() + ".tokenIsValid(String token)");
        try {
            Claims claims = getClaims(token);
            Date expiration = claims.getExpiration();
            Date now = new Date(System.currentTimeMillis());
            return expiration.after(now);
        } catch (ExpiredJwtException ex) {
            return false;
        }
    }

    public String getUserUsername(String token) throws ExpiredJwtException {
        log.debug(this.getClass().getSimpleName() + ".getUserUsername(String token)");
        return getClaims(token).getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        log.debug(this.getClass().getSimpleName() + ".generateToken(UserDetails userDetails)");
        long longExpiration = Long.parseLong(expiration);
        LocalDateTime ldtExpiration = LocalDateTime.now().plusMinutes(longExpiration);
        Instant instant = ldtExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date dateExpiration = Date.from(instant);

        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS512, signature)
                .compact();

        return token;
    }
}
