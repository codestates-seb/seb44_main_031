package competnion.global.util;

import competnion.global.security.jwt.JwtTokenizer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import lombok.extern.slf4j.Slf4j;


import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
public class JwtUtils {
    private final JwtTokenizer jwtTokenizer;

    public JwtUtils(JwtTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    public Map<String, Object> getJwsClaimsFromRequest(HttpServletRequest request) {

        log.info("JwtUtils - getJwsClaimsFromRequest 실행");
        log.info("=================================================================================");
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        log.info(claims.toString());

        return claims;
    }

    public long getRemainingTokenExpiration(HttpServletRequest request) {

        log.info("JwtTokenizer - getRemainingTokenExpiration");
        log.info("====================================");

        JwtUtils jwtUtils = new JwtUtils(jwtTokenizer);

        log.info("exp 값 구하기");
        String expiration = jwtUtils.getJwsClaimsFromRequest(request).get((String) "exp").toString();
        log.info(expiration);


        long now =  new Date().getTime()/1000;

        log.info("now : " + now);
        log.info("remainingTime : " + (Integer.parseInt(expiration)-now)/60);
        return (Long.parseLong(expiration)-now)/60;
    }
}