package com.redislock.common.aspect;

import java.lang.annotation.*;

/**
 * @ProjectName: springboot-redisson-demo
 * @Package: com.redislock.common.aspect
 * @ClassName: RedissonHandleRepectSubmit
 * @Author: zhangqiang
 * @Description:
 * @Date: 2020/5/26 5:49 下午
 * @Version: 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedissonHandleRepectSubmit {
    int time() default 10;
}
