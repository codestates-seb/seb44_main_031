package competnion.domain.user.service;

import competnion.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

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
