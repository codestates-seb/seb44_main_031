package competnion.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    INVALID_INPUT_VALUE(400,"Invalid Input Value"),

    ACCESS_NOT_ALLOWED(401, "YOU ARE NOT OWNER OF THIS CONTENT"),
    ACCESS_TOKEN_EXPIRED(401,"ACCESS TOKEN EXPIRED"),
    ACCESS_TOKEN_REGISTERED_LOGOUT(401,"ACCESS TOKEN REGISTERED LOGOUT"),
    REFRESH_TOKEN_TAMPERED(401, "REFRESH TOKEN NOT FOUND IN DB"),
    REFRESH_TOKEN_NULL(401, "REFRESH TOKEN IS NOT FOUND IN HEADER"),

    USER_NOT_FOUND(404, "USER NOT FOUND");

    @Getter
    private final int status;
    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
