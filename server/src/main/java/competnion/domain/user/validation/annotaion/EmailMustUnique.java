package competnion.domain.user.validation.annotaion;

import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailMustUnique {
    String message() default "중복된 이메일 입니다.";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload() default {};
}
