package competnion.infra.redis.lock;

import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.service.CommunityService;
import competnion.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
@RequiredArgsConstructor
public class AttendLockFacade {
    private final CommunityService communityService;
    private final RedissonClient redissonClient;

    public void attend(final User user, final AttendRequest request) {
        RLock lock = redissonClient.getLock(String.format("attend : %d", user.getId()));

        try {
            boolean available = lock.tryLock(100, 2, SECONDS);
            if (!available)
                throw new RuntimeException("Lock 획득 실패");
            communityService.attend(user, request);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
