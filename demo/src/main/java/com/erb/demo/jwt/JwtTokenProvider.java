package com.erb.demo.jwt;
import com.erb.demo.security.CustomerUserDetails;
import com.erb.demo.security.EmployeeUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final String jwtSecret = "yourSecretKey12345678901234567890";
    private final long jwtExpirationInMs = 3600000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
/*
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
*/
    public String getUsernameFromJWT(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

public String generateToken(UserDetails userDetails) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

    Map<String, Object> claims = new HashMap<>();
    claims.put("sub", userDetails.getUsername());

    if (userDetails instanceof EmployeeUserDetails) {

        claims.put("role", ((EmployeeUserDetails) userDetails).getEmployee().getRank().name());
    } else if (userDetails instanceof CustomerUserDetails) {
        claims.put("role", "CUSTOMER");
    }

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
}
    public String getRoleFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

}
