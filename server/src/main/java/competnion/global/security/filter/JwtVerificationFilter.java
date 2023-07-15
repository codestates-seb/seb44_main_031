package competnion.global.security.filter;

import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ExceptionCode;
import competnion.global.util.CustomAuthorityUtils;
import competnion.global.util.JwtUtils;
import competnion.infra.redis.util.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomAuthorityUtils authorityUtils;
    private final RedisUtil redisUtil;

    public JwtVerificationFilter(JwtUtils jwtUtils, CustomAuthorityUtils authorityUtils, RedisUtil redisUtil) {
        this.jwtUtils = jwtUtils;
        this.authorityUtils = authorityUtils;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void doFilterInternal(    // 인증&권한이 필요한 요청이 있을 때 실행
                                        HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException {

        log.info("JWT 검증 : JwtVerificationFilter - doFilterInternal");
        log.info("Header : Authorization , Bearer 있는 상태임");
        log.info("===========================================================");


        try {

            try {
                log.info("1. Signature 검증 - 문자열로 암호화된 claim 파싱");
                Map<String,Object> claims = jwtUtils.getJwsClaimsFromRequest(request);
                log.info("Signature 검증 성공!");

                log.info("=============================================================");

                log.info("2. 해당 AccessToken의 Logout BlackList 등록 여부 확인");
                if (redisUtil.hasKeyBlackList(request.getHeader("Authorization").replace("Bearer ",""))) {

                    throw new BusinessLogicException(ExceptionCode.ACCESS_TOKEN_REGISTERED_LOGOUT);
                }
                log.info("=============================================================");

                log.info("3. claim ->  SecurityContext 저장을 시도합니다");
                setAuthenticationToContext(claims);
            }
            catch (ExpiredJwtException e) {
                /**
                    Runtime Error -> BusinessLogicException으로 바꿔서 던지기
                    401에러 중에서도 토큰 만료 상황은 재발급을 메세지를 추가로 띄워줘서 구별한다.
                    FE는 401 에러 중에서도 response body에 만료 메세지 받으면 재발급 요청 날릴 수 있겠끔
                 **/

                throw new BusinessLogicException(ExceptionCode.ACCESS_TOKEN_EXPIRED);
            }
            catch (BusinessLogicException be) {
                // 로그아웃 잡아내는 용도
                throw new BusinessLogicException(ExceptionCode.ACCESS_TOKEN_REGISTERED_LOGOUT);
            }


        }
        catch (SignatureException se) {
            log.info("SignatureException 발생!");
            request.setAttribute("exception",se);

        }
        catch (BusinessLogicException be) {
            log.info("Business Exception 발생 !");
            request.setAttribute("exception",be);
        }
        catch (Exception e) {
            log.info("아무튼 에러 발생!");
            request.setAttribute("exception",e);
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        log.info("JwtVerificationFilter - shouldNotFilter");
        log.info("=====================================================");

        log.info(" Checking result == false 일 때 doFilterInternal 실행");


        String authorization = request.getHeader("Authorization");
        String refresh = request.getHeader("Refresh");

        if (refresh != null && authorization != null && authorization.startsWith("Bearer")) {
            log.info("Access, Refresh 둘 다 있으므로 재발급 요청입니다.");
            return true;
        }
        else {

            boolean check = authorization == null || !authorization.startsWith("Bearer");
            log.info("Checking result = " + check);

            return authorization == null || !authorization.startsWith("Bearer");
        }
    }

    private void setAuthenticationToContext(Map<String,Object> claims) {

        log.info("JwtVerificationFilter - setAuthenticationToContext 실행");
        log.info("======================================================================");

        log.info("Request AccessToken Payload ->  userEmail value 값 추출 ");
        String userEmail = (String) claims.get("username");

        log.info("Request AccessToken Payload ->  roles value List값 추출 + 가공 ");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));
        log.info("authorities : {}",authorities);

        log.info("Request AccessToken Payload -> 인증 검사의 대상이되는 토큰 생성");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userEmail,null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }
}
