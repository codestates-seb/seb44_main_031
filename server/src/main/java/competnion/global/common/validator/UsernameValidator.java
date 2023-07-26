package competnion.global.common.validator;

import competnion.global.common.annotation.ValidUsername;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9가-힣]){2,10}$");
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
