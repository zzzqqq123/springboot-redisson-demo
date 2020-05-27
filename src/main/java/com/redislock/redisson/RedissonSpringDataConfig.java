package com.redislock.redisson;

import org.apache.tomcat.util.file.ConfigurationSource;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @ProjectName: springboot-redisson-demo
 * @Package: com.redislock
 * @ClassName: RedissonSpringDataConfig
 * @Author: zhangqiang
 * @Description:
 * @Date: 2020/5/26 1:55 上午
 * @Version: 1.0
 */
@Configuration
public class RedissonSpringDataConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useClusterServers().addNodeAddress("redis://127.0.0.1:30001","redis://127.0.0.1:30002","redis://127.0.0.1:30003","redis://127.0.0.1:30004","redis://127.0.0.1:30005","redis://127.0.0.1:30006");

        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;

    }


}