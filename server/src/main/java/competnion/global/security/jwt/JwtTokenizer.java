package competnion.global.security.jwt;

import competnion.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenizer {

    @Getter
    @Value("${jwt.key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;



    // TEXT -> BASE64 문자열 인코딩
    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // JWT 시그니처에 사용할 SecretKey 생성
    public Key getKeyFromBase64EncodedKey (String encodedSecretKey) {

        byte[] keyBytes = Decoders.BASE64.decode(encodedSecretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }
    // 인증된 사용자에게 JWT 생성 및 발급
    public String generateAccessToken(
            Map<String,Object> claims,
            String subject,
            Date expiration,
            String encodedSecretKey) {


        log.info("JwtTokenizer - generateAccessToken 실행");

        Key key = getKeyFromBase64EncodedKey(encodedSecretKey);


        log.info("AccessToken payload 구성");
        return Jwts.builder()
                .setClaims(claims) // JWT payload 중 클레임
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key) // JWT signature 중 서명
                .compact(); // JWT 생성 및 직렬화
    }

    public String generateRefreshToken(String subject, Date expiration, String encodedSecretKey) {

        log.info("JwtTokenizer - generateRefreshToken 실행");
        Key key = getKeyFromBase64EncodedKey(encodedSecretKey);


        log.info("RefreshToken 생성");
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    public Jws<Claims> getClaims(String jws, String encodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(encodedSecretKey);

        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);

        return claims;
    }

    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    // Jwt Signature 검증하므로써 토큰 위,변조 검사
    public void verifySignature(String jws, String encodedSecretKey) {

        Key key = getKeyFromBase64EncodedKey(encodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    public String delegateAccessToken(User user) {

        log.info("JwtAuthenticationFilter - delegateAccessToken");

        // AccessToken 생성 로직
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",user.getId());
        claims.put("roles",user.getRoles());
        claims.put("username",user.getNickname());
        String subject = user.getEmail();
        Date expiration = getTokenExpiration(getAccessTokenExpirationMinutes());
        String encodedSecretKey = encodeBase64SecretKey(getSecretKey());

        String accessToken = generateAccessToken(claims,subject,expiration,encodedSecretKey);

        return accessToken;

    }

    public String delegateRefreshToken(User user) {
        String subject = user.getEmail();
        Date expiration = getTokenExpiration(getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = encodeBase64SecretKey(getSecretKey());

        String refreshToken = generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }


}
