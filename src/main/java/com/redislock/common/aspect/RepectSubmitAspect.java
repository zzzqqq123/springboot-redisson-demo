package com.redislock.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: springboot-redisson-demo
 * @Package: com.redislock.common.aspect
 * @ClassName: RepectSubmitAspect
 * @Author: zhangqiang
 * @Description:
 * @Date: 2020/5/26 5:51 下午
 * @Version: 1.0
 */
@Aspect
@Component
public class RepectSubmitAspect {
    @Autowired
    private RedissonClient redissonClient;

    @Pointcut("@annotation(redissonHandleRepectSubmit)")
    public void pointCut(RedissonHandleRepectSubmit redissonHandleRepectSubmit) {

    }

    @Around("pointCut(redissonHandleRepectSubmit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint,
                         RedissonHandleRepectSubmit redissonHandleRepectSubmit) throws Throwable {
        RLock lock = redissonClient.getLock(this.getKey());

        try {
            boolean b = lock.tryLock(50, TimeUnit.MILLISECONDS);
            if (b) {
                Object proceed = proceedingJoinPoint.proceed();
                return proceed;
            } else {
                return "表单重复提交，请稍后重试";
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                System.out.println("释放锁");
                lock.unlock();
            }
        }
        return null;

    }

    private String getKey() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader("token");
        String path = request.getServletPath();
        return token + path;
    }

}
