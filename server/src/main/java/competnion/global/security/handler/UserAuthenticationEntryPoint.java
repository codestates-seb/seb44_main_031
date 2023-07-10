package competnion.global.security.handler;

import competnion.global.exception.BusinessLogicException;
import competnion.global.exception.ErrorResponder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class UserAuthenticationEntryPoint implements AuthenticationEntryPoint {
    // Exception 발생으로 SecurityContext에 Authentication이 저장되지 않을 경우의 handler

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {


        log.info("AuthenticationEntryPoint 실행");
        log.info("=======================================");

        Exception exception = (Exception) request.getAttribute("exception"); // Object -> Exception

        if (exception instanceof BusinessLogicException) {
            ErrorResponder.sendBusinessErrorResponse(response,((BusinessLogicException) exception).getExceptionCode());
            logExceptionMessage(authException,exception);
        }
        else {
            ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
            logExceptionMessage(authException, exception);
        }

    }


    private void logExceptionMessage(AuthenticationException authException, Exception exception) {
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("Unauthorized error happened : {}",message);
    }

}
