package competnion.domain.user.service;

import competnion.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static competnion.global.exception.ErrorCode.INVALID_INPUT_VALUE;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private JavaMailSender javaMailSender;

    /**
     * TODO : 예외처리 에러코드 추가 필요
     */
    public void sendEmail(String email, String title, String content) {
        SimpleMailMessage emailForm = createEmailForm(email, title, content);

        try {
            javaMailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new BusinessException(INVALID_INPUT_VALUE);
        }
    }

    private SimpleMailMessage createEmailForm(String email, String title, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(content);

        return simpleMailMessage;
    }
}
