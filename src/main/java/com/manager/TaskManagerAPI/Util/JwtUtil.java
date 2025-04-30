package com.manager.TaskManagerAPI.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.manager.TaskManagerAPI.constants.AppConstants.SECRET_KEY;
import static com.manager.TaskManagerAPI.constants.AppConstants.JWT_EXPIRATION;

@Component
public class JwtUtil {

    /**
     *
     * @param token
     * @param claimsResolver
     * @return
     * @param <T>
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claim = getAllClaims(token);
        return claimsResolver.apply(claim);
    }

    /**
     *
     * @param token
     * @return
     */
    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     *
     * @param usernameToken
     * @return
     */
    public String extractUsername(String usernameToken) {
        return getClaim(usernameToken, Claims::getSubject);
    }

    /**
     *
     * @param token
     * @return
     */
    public Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     *
     * @param userDetails
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     *
     * @param claims
     * @param username
     * @return
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    /**
     *
     * @param token
     * @param userDetails
     * @return
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getClaim(token, Claims::getSubject);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
