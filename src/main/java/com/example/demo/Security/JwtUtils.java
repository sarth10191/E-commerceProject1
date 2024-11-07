package com.example.demo.Security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Parse Request to identify header, check if api contract is followed
     * (in this case having "Bearer " in start of token string.)
     * and get the token and return.*/
    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken!=null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
    /**
     * @param token
     * Returns username from token. Structure of our token has subject set to username. So get subject after parsing the token.*/
    public String getUserNameFromToken(String token){
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    /**
     * Generate key of type SecretKey from secret key using Specified algorithm.*/
    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    /**
     * Validates token by parsing checking expiry, if username exists, authorities, and other account details if any.
     * All these are collectively called as claims. Also checks the signature with hash function.*/
    public boolean validateToken(String jwtToken){
        try{
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(jwtToken);
            return true;
        }
        catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e){
            System.out.println(jwtToken + "\n" +e.getMessage());
        }
        return false;
    }
    /**
     * @param userDetails
     * @return JwtToken
     * Issue time and Expiration limit is added and signed with hash function.*/
    public String generateTokenFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

}
