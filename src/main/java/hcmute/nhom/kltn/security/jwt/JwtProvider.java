package hcmute.nhom.kltn.security.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import hcmute.nhom.kltn.common.payload.JwtAuthenticationResponse;
import hcmute.nhom.kltn.security.pricipal.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * Class JwtProvider.
 *
 * @author: ThanhTrong
 * @function_id:
 * @version:
 **/
@Component
public class JwtProvider {
    public static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${app.jwtRefreshExpirationInMs}")
    private int jwtRefreshExpirationInMs;

    /**
     * createToken.
     * @param authentication authentication
     * @return JwtAuthenticationResponse
     */
    public JwtAuthenticationResponse createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 180000000);
        Date expiryRefreshDate = new Date(now.getTime() + 180000000);
        Predicate<GrantedAuthority> filter = item -> item.getAuthority().equals("ADMIN");
        Collection<GrantedAuthority> roles =
                userPrincipal.getAuthorities().stream().filter(filter).collect(Collectors.toList());
        boolean admin = false;
        if (!roles.isEmpty()) {
            admin = true;
        }
        String accessToken = Jwts.builder().setSubject(userPrincipal.getUsername())
                .claim("admin", admin)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        String refreshToken = Jwts.builder().setSubject(userPrincipal.getUsername())
                .claim("admin", admin)
                .setIssuedAt(new Date())
                .setExpiration(expiryRefreshDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * validateToken.
     * @param authToken authToken
     * @return boolean
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            logger.info("Token true");
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
