package com.example.MyToss.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisRepositoryConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config(); // 주의! redisson.config 패키지의 것을 사용
        String url = "redis://" + redisHost + ":" + redisPort;
        config.useSingleServer().setAddress(url);
        return Redisson.create(config);
    }
}
