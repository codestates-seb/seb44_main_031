package competnion.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PRIVATE;

@Getter
@JsonInclude(NON_NULL)
@AllArgsConstructor(access = PRIVATE)
public class Response<T> {
    private static final String SUCCESS_MESSAGE = "요청이 성공했습니다.";
    private String success;
    private T result;

    public static<T> Response<T> success() {
        return new Response<>(SUCCESS_MESSAGE, null);
    }

    public static<T> Response<T> success(T data) {
        return new Response<>(SUCCESS_MESSAGE, data);
    }
}
