package at.aau.recipeorganizer.configuration.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

// TODO move all of the finals into the properties file
@Component
public class JwtUtils {

    private static final String jwtSecret = "VERYSECRETVALUEVERYSECRETVALUEVERYSECRETVALUEVERYSECRETVALUEVERYSECRETVALUEVERYSECRETVALUEVERYSECRETVALUEVERYSECRETVALUE";

    private static final int jwtExpirationMs = 100000000;

    private static final String jwtCookie = "JWT_TOKEN";

    public Optional<String> getJwtFromCookies(HttpServletRequest request) {
        return Optional.ofNullable(WebUtils.getCookie(request, jwtCookie)).map(Cookie::getValue);
    }

    public ResponseCookie generateJwtCookie(UserDetails userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
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