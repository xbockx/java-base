package com.linbo.boot.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Jwts;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author xbockx
 * @Date 1/10/2022
 */
public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public String createToken(UserDetails userDetails) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setId("linbo")
                .claim("authorities", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 60_000))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

}
