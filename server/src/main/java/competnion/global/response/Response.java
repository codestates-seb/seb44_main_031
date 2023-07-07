package competnion.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response {
    private boolean success;
    private Result result;

    public static Response success() {
        return new Response(true, null);
    }

    public static <T> Response success(T data) {
        return new Response(true, new Success<>(data));
    }

    public static Response failure(String message) {
        return new Response(false, new Failure(message));
    }
}
