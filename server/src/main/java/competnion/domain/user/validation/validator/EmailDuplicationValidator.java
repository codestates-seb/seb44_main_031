package competnion.domain.user.validation.validator;

import competnion.domain.user.repository.UserRepository;
import competnion.domain.user.validation.annotaion.EmailMustUnique;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@Component
@RequiredArgsConstructor
public class EmailDuplicationValidator implements ConstraintValidator<EmailMustUnique, String> {

    private final UserRepository userRepository;

    @Override
    public void initialize(EmailMustUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        final Boolean isExistEmail = userRepository.existsByEmail(email);

        if (isExistEmail) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("Email: {0} 이미 존재하는 이메일 입니다", email)
            )
                    .addConstraintViolation();
        }

        return !isExistEmail;
    }
}
