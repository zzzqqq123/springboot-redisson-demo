package com.redislock;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringbootRedissonDemoApplicationTests {
    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 10; i++) {
            RBucket<Object> ss = redissonClient.getBucket("key");
            System.out.println(ss.get());
            if (null != ss && StringUtils.isEmpty(ss.get())) {
                ss.set("test");
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ss.getAndDelete();
                }

            } else {
                ss.getAndDelete();
                System.out.println("正在处理数据请稍后");
                System.out.println(i);
            }
        }
    }

    @Test
    public void test2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new RedisSessonTest(redissonClient));
            executorService.execute(thread);
        }

    }

    class RedisSessonTest implements Runnable {
        private RedissonClient redissonClient;

        RedisSessonTest(RedissonClient redissonClient) {
            this.redissonClient = redissonClient;
        }


        @Override
        public void run() {
            RLock lock = redissonClient.getLock("lock");
            lock.lock(10, TimeUnit.SECONDS);
            if (lock.isLocked()) {
                System.out.println("数据已经被枷锁");
            }
            System.out.println("加锁");


            System.out.println("加锁完成");
            try {
                //执行业务逻辑
                Thread.sleep(10000);
                System.out.println(123);

            } catch (Exception e) {

            } finally {
                lock.unlock();
            }
            try {
                Thread.sleep(1000000);
            } catch (Exception e) {

            }
        }
    }

}

