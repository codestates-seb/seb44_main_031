package competnion.global.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private String message;
    private ErrorCode errorCode;

    @Builder
    private ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponse withMessage(ErrorCode errorCode, String message) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
    }
}
