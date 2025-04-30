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

/**
 * Utility class for handling JWT token validation, generation, parsing, and expiration
 * Central class for authentication for task API
 */
@Component
public class JwtUtil {

    /**
     * extracts a specific claim from the entered token using some helper functions
     * @param token             The JWT token
     * @param claimsResolver    function to extract a specific claim
     * @param <T>               the type of claim
     * @return extracted claim
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claim = getAllClaims(token);
        return claimsResolver.apply(claim);
    }

    /**
     * extracts all claims from a token with a secret key
     * @param token JWT token
     * @return claim from the token
     */
    public Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * extracts subject (username) from the token
     * @param usernameToken JWT token
     * @return the username as String
     */
    public String extractUsername(String usernameToken) {
        return getClaim(usernameToken, Claims::getSubject);
    }

    /**
     * extracts the expiration dat of the given token
     * @param token the JWT token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * checks if the token is expired based on expiration claim
     * @param token JWT token
     * @return true if the token is expired else false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * generates the JWT token with given user details
     * @param userDetails user details containing username
     * @return  generates the JWT token as String
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Create the actual JWT token by using the claims and the username
     * @param claims    Map of additional claims
     * @param username  the username to set as a subject
     * @return signed JWT as String
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }

    /**
     * validates token by checking its username and expiration date
     * @param token         JWT token
     * @param userDetails   comparing the username against the token
     * @return true is its valid, else false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getClaim(token, Claims::getSubject);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
