package competnion.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        log.error("BusinessException : {}, {}", e.getErrorCode(), e.getErrorCode().getMessage());
        ErrorResponse response = ErrorResponse.from(e.getErrorCode());

        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(response);
    }

//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleUnexpectedException(Exception e) {
//        log.error("UnexpectedException : {}", e.getMessage());
//
//        return ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(BAD_REQUEST)
//    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//        final String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//        log.error("ValidateFailed : {}", message);
//
//        return ErrorResponse.withMessage(ErrorCode.REQUEST_VALIDATION_FAIL, message);
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    @ResponseStatus(BAD_REQUEST)
//    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
//        final String message = e.getMessage();
//        log.error("ValidateFailed : {}", message);
//
//        return new ErrorResponse(ErrorCode.REQUEST_VALIDATION_FAIL, message);
//    }
}
