package com.revature.toDoList.auth;

import com.revature.toDoList.config.JwtConfig;
import com.revature.toDoList.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final Key key;
    private final long expiration;

    public JwtService(JwtConfig jwtConfig){
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expiration = jwtConfig.getExpirationMs();
    }

    public long getExpirationMs(){
        return expiration;
    }

    public String generateToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);


        String role = user.getRole();
        if (role == null || role.isBlank()) {
            role = "USER";
        }
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }


        return Jwts.builder()
                .setSubject(user.getUserId())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("role",role)
                .claim("username",user.getUsername())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserId(String token){
        return parseClaims(token).getBody().getSubject();
    }
    public String extractUsername(String token){
        return parseClaims(token).getBody().get("username",String.class);
    }

    private boolean isExpired(String token) {
        Date exp = parseClaims(token).getBody().getExpiration();
        return exp.before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

    }


}
