package com.substring.auth.auth_app_backend.security;

import com.substring.auth.auth_app_backend.entities.Role;
import com.substring.auth.auth_app_backend.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final SecretKey key;
    private final long accessTtlSeconds;
    private final long refreshTtlSeconds;
    private final String issuer;

    public JwtService(@Value("${security.jwt.secret}") String secretkey,
                      @Value("${security.jwt.access-ttl-seconds}") long accessTtlSeconds,
                      @Value("${security.jwt.refresh-ttl-seconds}")long refreshTtlSeconds,
                      @Value("${security.jwt.issuer}")String issuer) {

        if(secretkey == null || secretkey.length() < 64){
            throw new IllegalArgumentException("Invalid Secret");
        }
        this.key = Keys.hmacShaKeyFor(secretkey.getBytes(StandardCharsets.UTF_8));
        this.accessTtlSeconds = accessTtlSeconds;
        this.refreshTtlSeconds = refreshTtlSeconds;
        this.issuer = issuer;
    }

    //generate Token
    public String generateAccessToken(User user){
        Instant now = Instant.now();
        List<String> roles = user.getRoles() ==null?List.of():
                user.getRoles().stream().map(Role::getName).toList();

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claims(Map.of(
                        "email",user.getEmail(),
                        "roles",roles,
                        "type","access"
                )).signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    //refresh Token
    public String generateRefreshToken(User user, String id){
        Instant now = Instant.now();

        return Jwts.builder()
                .id(id)
                .subject(user.getId().toString())
                .issuer(issuer)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtlSeconds)))
                .claim("type","refresh")
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    //parse the token
    public Jws<Claims> parse(String token){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public boolean isAccessToken(String token){
        Claims val = parse(token).getPayload();
        return "access".equals(val.get("type"));
    }

    public boolean isRefreshToken(String token){
        Claims val = parse(token).getPayload();
        return "refresh".equals(val.get("type"));
    }

    public UUID getUserId(String token){
        Claims val = parse(token).getPayload();
        return UUID.fromString(val.getSubject());
    }

    public String getEmail(String token){
        Claims val = parse(token).getPayload();
        return val.get("email").toString();
    }

    public List<String> getRoles(String token){
        Claims val = parse(token).getPayload();
        return (List<String>) val.get("roles");
    }

    public UUID getJti(String token){
        return UUID.fromString(parse(token).getPayload().getId());
    }

}
