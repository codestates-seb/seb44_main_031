package competnion.domain.user.annotation;

import competnion.domain.user.annotation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default "비밀번호는 특수문자, 영문자, 숫자 포함 8~20자 여야 합니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
