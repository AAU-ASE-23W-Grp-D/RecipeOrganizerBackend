package at.aau.recipeorganizer.configuration.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {

	@Value("${jwt.secret}")
    private String jwtSecret;

	@Value("${jwt.expirationMs}")
    private int jwtExpirationMs;

    public static final String JWT_COOKIE = "JWT_TOKEN";

    public Optional<String> getJwtFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(JWT_COOKIE));
    }

    public ResponseCookie generateJwtCookie(UserDetails userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(JWT_COOKIE, jwt).path("/api").maxAge((long)24 * 60 * 60).httpOnly(true).build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        // TODO better exception handling
        try {
            Jwts.parser().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateTokenFromUsername(String username) {
        Date issued = new Date();
        return Jwts.builder()
                .subject(username)
                .issuedAt(issued)
                .expiration(new Date(issued.getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}