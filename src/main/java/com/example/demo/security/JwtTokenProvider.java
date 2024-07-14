package com.example.demo.security;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.constants.Constants;
import com.example.demo.exception.APIException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateToken(Authentication authentication) {
        try {
            String email = authentication.getName();
            Date currentDate = new Date();
            Date expireDate = new Date(currentDate.getTime() + 36000000); // 10 hours expiration

            String token = Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(currentDate)
                    .setExpiration(expireDate)
                    .signWith(Constants.JwtSecretKey, SignatureAlgorithm.HS256)
                    .compact();

            logger.info("Generated JWT Token for user: {}", email);

            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT Token", e);
            throw new APIException("Error generating JWT Token: " + e.getMessage());
        }
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(Constants.JwtSecretKey).build()
                    .parseClaimsJws(token).getBody();

            String email = claims.getSubject();
            logger.info("Extracted email from JWT Token: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("Error extracting email from JWT Token", e);
            throw new APIException("Error extracting email from JWT Token: " + e.getMessage());
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(Constants.JwtSecretKey).build().parseClaimsJws(token);
            logger.info("JWT Token validation successful");
            return true;
        } catch (Exception e) {
            logger.error("Error validating JWT Token", e);
            throw new APIException("Token validation Issue: " + e.getMessage());
        }
    }
}
