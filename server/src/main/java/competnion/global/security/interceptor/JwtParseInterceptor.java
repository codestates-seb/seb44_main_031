package competnion.global.security.interceptor;

import competnion.global.exception.ErrorResponder;
import competnion.global.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
public class JwtParseInterceptor implements HandlerInterceptor {
    private final JwtUtils jwtUtils;
    private static final ThreadLocal<Long> authenticatedUserId = new ThreadLocal<>();

    public JwtParseInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public static long getAuthenticatedUserId() {
        return authenticatedUserId.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {

            if (request.getHeader("Authorization")==null) { return true;}

            // 로그인 된 유저의 요청에서만 토큰에서 userId 추출
            else {
                log.info("JwtParser prehandle 실행");
                Map<String, Object> claims = jwtUtils.getJwsClaimsFromRequest(request);
                log.info("claims : "+ claims);
                authenticatedUserId.set(Long.valueOf(claims.get("userId").toString()));
                return true;
            }

        } catch (Exception e) {
            log.info("e : " +e);
            ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) {
        this.authenticatedUserId.remove();
    }
}
