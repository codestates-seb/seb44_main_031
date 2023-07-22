package competnion.domain.auth.event;

import competnion.infra.mail.util.EmailUtil;
import competnion.infra.redis.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthEventHandler {
    private final EmailUtil emailUtil;
    private final RedisUtil redisUtil;

    @Async
    @EventListener
    public void authEmailEventListner(final AuthEmailEvent authEmailEvent) {
        final String code = emailUtil.generateRandomCode();
        redisUtil.setDataAndExpire(authEmailEvent.getEmail(), code, 600000L);
        emailUtil.sendVerificationEmail(authEmailEvent.getEmail(), code);
    }
}
