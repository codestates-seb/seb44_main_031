package competnion.global.common.aop;

import competnion.global.common.annotation.Lock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LockAop {
    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(competnion.global.common.annotation.Lock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Lock lock = method.getAnnotation(Lock.class);

        RLock rLock = redissonClient.getLock(lock.key());

        try {
            boolean available = rLock.tryLock(lock.waitTime(), lock.leaseTime(), lock.timeUnit());
            if (!available) return false;

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            rLock.unlock();
        }
    }
}
