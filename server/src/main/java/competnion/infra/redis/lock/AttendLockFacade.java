package competnion.infra.redis.lock;

import competnion.domain.community.dto.request.AttendRequest;
import competnion.domain.community.service.CommunityService;
import competnion.domain.user.entity.User;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static java.util.concurrent.TimeUnit.SECONDS;

@Service
public class AttendLockFacade {
    private final CommunityService communityService;
    private final RedissonClient redissonClient;

    public AttendLockFacade(final CommunityService communityService, final RedissonClient redissonClient) {
        this.communityService = communityService;
        this.redissonClient = redissonClient;
    }

    public void attend(final User user, final AttendRequest request, final Long articleId) {
        RLock lock = redissonClient.getLock(String.format("attend : %s", user.getNickname()));

        try {
            boolean available = lock.tryLock(10, 1, SECONDS);
            if (!available) {
                throw new IllegalArgumentException("Lock 획득 실패");
            }
            communityService.attendArticle(user, request, articleId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
