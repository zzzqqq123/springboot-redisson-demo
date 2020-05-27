package com.redislock.redisson;

import com.redislock.common.aspect.RedissonHandleRepectSubmit;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: springboot-redisson-demo
 * @Package: com.redislock
 * @ClassName: WebController
 * @Author: zhangqiang
 * @Description:
 * @Date: 2020/5/26 12:54 下午
 * @Version: 1.0
 */
@RestController
@RequestMapping("test")
public class RdissonController {
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("redissontest")
    @RedissonHandleRepectSubmit(time = 10)
    public String test() throws InterruptedException {
        Thread.sleep(3000);
        return "123";
    }

    class RedisSessonTest implements Runnable {
        private RedissonClient redissonClient;

        RedisSessonTest(RedissonClient redissonClient) {
            this.redissonClient = redissonClient;
        }


        @Override
        public void run() {
            RLock lock = redissonClient.getLock("lock");

            try {
                boolean tryLock = lock.tryLock(5, TimeUnit.SECONDS);
                if (tryLock) {
                    //执行业务逻辑
                    System.out.println("开始执行业务");
                    Thread.sleep(2000);
                    System.out.println("业务执行完成");
                    lock.unlock();
                } else {
                    System.out.println("请稍后操作");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }


        }
    }
}
