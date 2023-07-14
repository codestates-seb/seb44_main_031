package competnion.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {
    private Boolean success;
    private T result;

    public static<T> Response<T> success() {
        return new Response<>(true, null);
    }

    public static<T> Response<T> success(T data) {
        return new Response<>(true, data);
    }
}
