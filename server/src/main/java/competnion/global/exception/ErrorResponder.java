package competnion.global.exception;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ErrorResponder {
    public static void sendErrorResponse(
            HttpServletResponse response,
            HttpStatus status) throws IOException {

        log.info("ErrorResponder - sendErrorResponse");

        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }

    public static void sendBusinessErrorResponse(
            HttpServletResponse response,
            ExceptionCode exceptionCode) throws IOException {

        log.info("ErrorResponder - sendBusinessErrorResponse");

        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));

    }
}
