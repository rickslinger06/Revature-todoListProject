package com.revature.toDoList.auth;

import com.revature.toDoList.config.JwtConfig;
import com.revature.toDoList.entity.User;
import com.revature.toDoList.exception.InvalidTokenException;
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
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtService(JwtConfig jwtConfig){
        this.key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
        this.accessExpirationMs = jwtConfig.getAccessExpirationMs();
        this.refreshExpirationMs = jwtConfig.getRefreshExpirationMs();
    }
    public String generateAccessToken(User user){
        return buildToken(user, accessExpirationMs,"access");
    }

    public String generateRefreshToken(User user){
        return buildToken(user, refreshExpirationMs,"refresh");
    }

    private String buildToken(User user,long expMs, String type){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expMs);


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
                .claim("type",type)
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

    public String extractType(String token){
        return parseClaims(token).getBody().get("type",String.class);
    }

    private boolean isExpired(String token) {
        Date exp = parseClaims(token).getBody().getExpiration();
        return exp.before(new Date());
    }

    public void assertTokenType(String token, String expectedType) {
        String type = extractType(token);
        if (type == null || !type.equals(expectedType)) {
            throw new InvalidTokenException("Invalid token type");
        }
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        if (!username.equals(userDetails.getUsername())) {
            throw new InvalidTokenException("Token username does not match user");
        }

        if (isExpired(token)) {
            throw new InvalidTokenException("Token is expired");
        }

        return true;
    }


    private Jws<Claims> parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token);

    }


}
