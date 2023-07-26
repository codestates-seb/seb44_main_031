package competnion.global.exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {
    private ExceptionCode exceptionCode;
    private String detailMessage;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public BusinessLogicException(ExceptionCode exceptionCode, String detailMessage) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.detailMessage = detailMessage;
    }
}
