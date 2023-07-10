package competnion.infra.mail.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Component
@Transactional
@RequiredArgsConstructor
public class EmailUtil {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String title, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content, true);

        javaMailSender.send(message);
    }

    @Async
    public void sendVerificationEmail(String email, String code) {
        try {
            sendEmail(
                    email,
                    "PETMILY 회원가입 인증 코드",
                    "인증 코드: " + code
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateRandomCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}
