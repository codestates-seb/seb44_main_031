package competnion.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_INPUT_VALUE(400, HttpStatus.CONFLICT, "C001", "Invalid Input Value")
    ;

    private final int httpStatus;
    private final HttpStatus httpStatusName;
    private final String httpStatusCode;
    private final String message;
}
